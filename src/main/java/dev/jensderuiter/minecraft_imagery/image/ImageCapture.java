package dev.jensderuiter.minecraft_imagery.image;

import dev.jensderuiter.minecraft_imagery.Util;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * A class to render a single photo of a Minecraft world and the players in it.
 * Should be instantiated once for every capture.
 * Use .render() to render and get the resulting image capture.
 * It is recommended to ALWAYS CALL .render() ASYNCHRONOUSLY as it can take a while.
 */
public class ImageCapture {

    Location location;
    List<Player> entities;
    ImageCaptureOptions options;

    BufferedImage image;
    Graphics2D graphics;
    Map<Player, List<RayTracedPoint2D>> playerOccurrences;

    /**
     * Creates an instance of the ImageCapture class.
     * @param location The location from which to capture the image.
     * @param entities A list of players that could be inside the photo.
     *                 Players will only be visible in the photo if they are actually in the viewing frame.
     */
    public ImageCapture(
            Location location,
            List<Player> entities,
            ImageCaptureOptions options
    ) {
        this.location = location.clone();
        this.entities = entities;
        this.options = options;

        this.image = new BufferedImage(
                this.options.getWidth(),
                this.options.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        this.graphics = this.image.createGraphics();
        this.playerOccurrences = new HashMap<>();
    }

    /**
     * Creates an instance of the ImageCapture class with no players and no options.
     * @param location The location from which to capture the image.
     */
    public ImageCapture(Location location) {
        this(location, new ArrayList<>(), ImageCaptureOptions.builder().build());
    }

    /**
     * Creates an instance of the ImageCapture class with no options.
     * @param location The location from which to capture the image.
     */
    public ImageCapture(Location location, List<Player> entities) {
        this(location, entities, ImageCaptureOptions.builder().build());
    }

    /**
     * Creates an instance of the ImageCapture class with no players.
     * @param location The location from which to capture the image.
     */
    public ImageCapture(Location location, ImageCaptureOptions options) {
        this(location, new ArrayList<>(), options);
    }

    /**
     * Render an image which the instance's location and entities.
     * It is recommended to ALWAYS CALL this method ASYNCHRONOUSLY as it can take a while.
     * @return The resulting image (128x128)
     */
    public BufferedImage render() {

        long startTime = System.currentTimeMillis();

        // get pitch and yaw of players head to calculate ray trace directions
        double pitch = -Math.toRadians(this.location.getPitch());
        double yaw = Math.toRadians(this.location.getYaw() + 90);

        this.initBackground();

        // loop through every pixel on map
        for (int x = 0; x < this.options.getWidth(); x++) {
            for (int y = 0; y < this.options.getHeight(); y++) {

                // calculate ray rotations
                double yrotate = -((y) * this.options.getFov() / this.options.getHeight() - (this.options.getFov() / 2));
                double xrotate = ((x) * this.options.getFov() / this.options.getWidth() - (this.options.getFov() / 2));

                Vector rayTraceVector = new Vector(Math.cos(yaw + xrotate) * Math.cos(pitch + yrotate),
                        Math.sin(pitch + yrotate), Math.sin(yaw + xrotate) * Math.cos(pitch + yrotate));

                RayTraceResult entityResult = this.rayTraceEntitiesFromList(this.location, rayTraceVector, 64);

                Location lookFrom = this.location.clone();
                double[] dye = new double[]{1, 1, 1};

                // max tries for blocks to look through
                for (int i = 0; i < 10; i++) {
                    RayTraceResult result = this.location.getWorld().rayTraceBlocks(
                            lookFrom, rayTraceVector, 256,
                            FluidCollisionMode.ALWAYS, false);

                    if (result == null) {
                        // no block was hit, so we will assume we are looking at the sky
                        break;
                    }

                    if (this.options.getExcludedBlocks().contains(result.getHitBlock().getType())) {
                        // we hit an excluded block. update position and keep looking
                        lookFrom = result.getHitPosition().toLocation(this.location.getWorld());
                        continue;
                    }

                    TranslucentBlock translucentBlock = this.options
                            .getTranslucentBlocks()
                            .get(result.getHitBlock().getType());

                    if (translucentBlock != null) {
                        // we hit a see-through block. update dye, update position and keep looking
                        dye = ImageUtil.applyToDye(dye, translucentBlock.dye, translucentBlock.factor);
                        lookFrom = result
                                .getHitPosition()
                                .add(rayTraceVector.normalize())
                                .toLocation(this.location.getWorld());
                        continue;
                    }

                    // darken the block when it's far away
                    if (this.options.isShowDepth()) {
                        double distance = this.location.distance(result.getHitBlock().getLocation());
                        dye = this.darkenDyeByDistance(dye, distance);
                    }

                    // color the pixel
                    this.colorWithDye(x, y, result, dye);
                    break;
                }

                if (entityResult == null) continue;

                Entity hitEntity = entityResult.getHitEntity();
                if (hitEntity instanceof Player hitPlayer) {
                    List<RayTracedPoint2D> pixelList = playerOccurrences.get(hitPlayer);
                    if (pixelList == null) {
                        pixelList = new ArrayList<>();
                    }
                    pixelList.add(new RayTracedPoint2D(
                            new Point2D.Float(x, y),
                            entityResult.getHitPosition().toLocation(this.location.getWorld())
                    ));
                    playerOccurrences.put(hitPlayer, pixelList);
                }
            }
        }

        for (Map.Entry<Player, List<RayTracedPoint2D>> playerEntry : playerOccurrences.entrySet()) {
            RayTracedPoint2D topLeft = playerEntry.getValue().get(0);
            Point2D topLeftPoint = topLeft.getPoint2D();

            RayTracedPoint2D bottomRight = playerEntry.getValue().get(playerEntry.getValue().size() - 1);
            Point2D bottomRightPoint = bottomRight.getPoint2D();

            Player player = playerEntry.getKey();

            BoundingBox playerBox = player.getBoundingBox();

            double imageCroppedTop = 0, imageCroppedBottom = 0, imageCroppedLeft = 0, imageCroppedRight = 0;

            // crops are needed when only a part of the player is in view
            // so that only the part that can be seen will be put in the image
            // (not the whole player texture, squeezed or stretched)

            if (topLeftPoint.getY() == 0
                || bottomRightPoint.getY() == this.options.getHeight() - 1) {

                double playerLocationTop = playerBox.getMaxY();
                double playerLocationBottom = playerBox.getMinY();

                // a player 1.8 blocks high
                // so the difference / 1.8 * <canvas width> would return the amount of pixels that can actually be seen
                imageCroppedTop = playerLocationTop > topLeft.getHitPosition().getY()
                        ? Math.abs(playerLocationTop - topLeft.getHitPosition().getY())
                        / 1.8 * (this.options.getHeight() - (bottomRightPoint.getY() - topLeftPoint.getY()))
                        : 0;

                imageCroppedBottom = playerLocationBottom < bottomRight.getHitPosition().getY()
                        ? Math.abs(playerLocationBottom - bottomRight.getHitPosition().getY())
                        / 1.8 * (this.options.getHeight() - (bottomRightPoint.getY() - topLeftPoint.getY()))
                        : 0;
            }

            if (topLeftPoint.getX() == 0 || bottomRightPoint.getX() == 127) {
                double imageCroppedX = playerBox.getWidthX() - ImageUtil.difference(
                        topLeft.getHitPosition().getX(),
                        bottomRight.getHitPosition().getX()
                );

                double imageCroppedZ = playerBox.getWidthZ() - ImageUtil.difference(
                        topLeft.getHitPosition().getZ(),
                        bottomRight.getHitPosition().getZ()
                );

                double combinedValue = Math.sqrt(Math.pow(imageCroppedX, 2) + Math.pow(imageCroppedZ, 2))
                        * this.options.getWidth();

                if (topLeftPoint.getX() == 0) imageCroppedLeft = combinedValue;
                if (bottomRightPoint.getX() == 127) imageCroppedRight = combinedValue;
            }

            int width = (int) (bottomRightPoint.getX() - topLeftPoint.getX() + 1
                    + imageCroppedLeft + imageCroppedRight);
            int height = (int) (bottomRightPoint.getY() - topLeftPoint.getY() + 1
                    + imageCroppedTop + imageCroppedBottom);

            float cameraYaw = ImageUtil.positiveYaw(this.location.getYaw());
            float playerYaw = ImageUtil.positiveYaw(player.getLocation().getYaw());

            boolean isToFront = !(playerYaw + 90 > cameraYaw && playerYaw - 90 < cameraYaw);

            BufferedImage combinedTexture = isToFront ? Util.getPlayerSkinFront(player) : Util.getPlayerSkinBack(player);

            graphics.drawImage(
                    combinedTexture,
                    (int) (topLeftPoint.getX() - imageCroppedLeft),
                    (int) (topLeftPoint.getY() - imageCroppedTop),
                    width,
                    height,
                    null
            );

        }

        Bukkit.getLogger().config("Image capture took " + (System.currentTimeMillis() - startTime) + "ms");
        return image;
    }

    /**
     * Initializes the image background with a sky color and sometimes a sun/moon.
     * This is dependent on the options given and when enabled, the current world time.
     */
    private void initBackground() {
        long worldTime = this.location.getWorld().getTime() % 24000;

        if (!this.options.isDayLightCycleAware()
                // this checks if it is daytime (06:00 - 18:00)
                || worldTime >= 0 && worldTime <= 12000) {
            this.graphics.setColor(this.options.getSkyColor());
        } else {
            this.graphics.setColor(this.options.getSkyColorNight());
        }

        this.graphics.fillRect(0, 0, this.options.getWidth(), this.options.getHeight());

    }

    /**
     * Color a single pixel using a ray trace result, which will be converted to a color.
     * The given dye will also be applied to that color.
     * @param x The x coordinate of the pixel to be filled.
     * @param y The y coordinate of the pixel to be filled.
     * @param result The ray trace result from which to gather the block information.
     * @param dye The dye to apply to the color for the block (3-element array).
     */
    private void colorWithDye(int x, int y, RayTraceResult result, double[] dye) {
        byte lightLevel = result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel();

        if(lightLevel > 0) {
            double shadowLevel = 15.0;

            for(int i = 0; i < dye.length; i++) {
                dye[i] = dye[i] * (lightLevel / shadowLevel);
            }
        }

        Color color = ImageUtil.colorFromType(result.getHitBlock(), dye);

        if (color != null) this.image.setRGB(x, y, color.getRGB());
    }

    /**
     * Darken a dye using a given distance.
     * The farther away, the darker the dye.
     * @param dye The dye to darken.
     * @param distance distance in blocks.
     * @return The darkened dye.
     */
    public double[] darkenDyeByDistance(double[] dye, double distance) {
        for(int i = 0; i < dye.length; i++) {
            dye[i] = dye[i] * (1 - (distance / 600));
        }
        return dye;
    }

    /**
     * Raytrace from a set list of entities.
     * @param start The location to start raytracing from.
     * @param direction The vector of the direction in which to raytrace.
     * @param maxDistance The maximum amount of blocks to raytrace in the direction.
     * @return A RayTraceResult with the first entity the raytrace has collided with, or null if there is none.
     */
    private RayTraceResult rayTraceEntitiesFromList(Location start, Vector direction, double maxDistance) {
        Vector startPos = start.toVector();
        Entity nearestHitEntity = null;
        RayTraceResult nearestHitResult = null;
        double nearestDistanceSq = Double.MAX_VALUE;
        Iterator var17 = entities.iterator();

        while(var17.hasNext()) {
            Entity entity = (Entity)var17.next();

            // entity is in the same position, so likely the one that is taking the picture
            if (ImageUtil.isWithinBlockIgnoreY(entity.getLocation(), this.location)) continue;
            BoundingBox boundingBox = entity.getBoundingBox();
            RayTraceResult hitResult = boundingBox.rayTrace(startPos, direction, maxDistance);
            if (hitResult != null) {
                double distanceSq = startPos.distanceSquared(hitResult.getHitPosition());
                if (distanceSq < nearestDistanceSq) {
                    nearestHitEntity = entity;
                    nearestHitResult = hitResult;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return nearestHitEntity == null ? null : new RayTraceResult(nearestHitResult.getHitPosition(), nearestHitEntity, nearestHitResult.getHitBlockFace());
    }

}
