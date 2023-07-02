package dev.jensderuiter.minecraft_imagery.video;


import dev.jensderuiter.minecraft_imagery.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A class to render a single video capture frame.
 * Should be instantiated once for every frame.
 * Use .render() to render and get the resulting image.
 * It is recommended to ALWAYS CALL .render() ASYNCHRONOUSLY as it can take a while.
 */
public class VideoFrameCapture {

    int BOX_RADIUS;

    int WIDTH;
    float HALF_WIDTH;
    int HEIGHT;
    float HALF_HEIGHT;

    Location location;
    List<Entity> entities;
    BlockFace facing;
    BoundingBox box;
    World world;
    BufferedImage image;
    Graphics2D graphics;

    /**
     * Creates an instance of the VideoFrameCapture class.
     * @param location The location from which to capture the frame.
     * @param facing The direction the camera should look at (NORTH, EAST, SOUTH, WEST).
     * @param entities A list of entities that can be drawn (only supports players).
     * @param box_radius The radius the camera should look in blocks.
     * @param width The width of the resulting image.
     * @param height The height of the resulting image.
     */
    public VideoFrameCapture(
            Location location,
            BlockFace facing,
            List<Entity> entities,
            int box_radius,
            int width,
            int height
    ) {
        this.location = location;
        this.world = location.getWorld();
        this.facing = facing;
        this.entities = entities;

        this.BOX_RADIUS = box_radius;
        this.WIDTH = width;
        this.HALF_WIDTH= (float) WIDTH / 2;
        this.HEIGHT = height;
        this.HALF_HEIGHT = (float) HEIGHT / 2;

        this.box = BoundingBox.of(
                // TODO: Only fetch blocks in the direction the player is looking at
                location.clone().add(new Vector(
                        -box_radius,
                        -2,
                        -box_radius
                )),
                location.clone().add(new Vector(
                        box_radius,
                        box_radius + 2,
                        box_radius
                ))
        );

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();
    }

    public BufferedImage render() {
        List<Entity> entities = this.entities.stream().filter(
                entity -> box.contains(entity.getLocation().toVector())).toList();

        for (double y = box.getMinY(); y <= box.getMaxY(); y++) {
            switch (facing) {
                case NORTH:
                    for (double z = box.getMinZ(); z <= box.getMaxZ() - (BOX_RADIUS - 1); z++) {
                        for (double x = box.getMinX(); x <= box.getMaxX(); x++) {
                            handleBlock(x, y, z);
                            handleEntities(entities, x, y, z);
                        }
                    }
                    break;
                case EAST:
                    for (double x = box.getMaxX(); x >= box.getMinX() + (BOX_RADIUS - 1); x--) {
                        for (double z = box.getMinZ(); z <= box.getMaxZ(); z++) {
                            handleBlock(x, y, z);
                            handleEntities(entities, x, y, z);
                        }
                    }
                    break;
                case SOUTH:
                    for (double z = box.getMaxZ(); z >= box.getMinZ() + (BOX_RADIUS - 1); z--) {
                        for (double x = box.getMinX(); x <= box.getMaxX(); x++) {
                            handleBlock(x, y, z);
                            handleEntities(entities, x, y, z);
                        }
                    }
                    break;
                case WEST:
                    for (double x = box.getMinX(); x <= box.getMaxY() - (BOX_RADIUS - 1); x--) {
                        for (double z = box.getMaxZ(); z >= box.getMinY(); z--) {
                            handleBlock(x, y, z);
                            handleEntities(entities, x, y, z);
                        }
                    }
                    break;
            }
        }

        graphics.dispose();

        int wMod = 1;
        int hMod = 1;
        switch (facing) {
            case NORTH:
            case EAST:
                hMod = -1;
                break;
            case SOUTH:
            case WEST:
                wMod = -1;
                hMod = -1;
                break;
        }

        AffineTransform tx = AffineTransform.getScaleInstance(
                wMod,
                hMod
        );
        tx.translate(
                wMod == 1 ? 0 : image.getWidth() * wMod,
                hMod == 1 ? 0 : image.getHeight() * hMod
        );
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return image;
    }

    /**
     * Handle a single block (write its texture on the canvas) with its given coordinates.
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The Z coordinate of the block.
     */
    private void handleBlock(double x, double y, double z) {
        Block block = world.getBlockAt(
                (int) x,
                (int) y,
                (int) z
        );

        if (!block.getType().isSolid()) return;

        int light = 0;
        final BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace blockFace : blockFaces) {
            light = Math.max(light, block.getRelative(blockFace).getLightLevel());
            if (light >= 15) {
                break;
            }
        }

        if (light < 5) return;

        double[] distances = getDistances(x, y, z);

        if (distances == null) return;

        double distanceFrontOfCamera = distances[0];
        double distanceSideOfCamera = distances[1];

        /*
        Bukkit.getLogger().info("Material: " + block.getType().getKey().getKey());
        Bukkit.getLogger().info("Front distance: " + distanceFrontOfCamera);
        Bukkit.getLogger().info("Side distance: " + distanceSideOfCamera);
        */

        BufferedImage texture = Util.getImage(block.getType().getKey().getKey());

        if (texture == null) return;

        Point2D topLeftPos = get2D(
                (int) distanceSideOfCamera,
                (int) (location.getY() - y + 1),
                (int) distanceFrontOfCamera
        );

        BufferedImage transformedTexture = new BufferedImage(
                texture.getWidth() * 3,
                texture.getHeight() * 3,
                BufferedImage.TYPE_INT_ARGB
        );
        transformedTexture.createGraphics().drawImage(
                texture,
                texture.getWidth(),
                texture.getHeight(),
                null
        );

        AffineTransform tx = new AffineTransform();
        tx.shear(-distanceSideOfCamera / (distanceFrontOfCamera * 3 + 8), 0);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        transformedTexture = op.filter(transformedTexture, null);

        int newDimensions = (int) Math.abs(Math.floor(16 * 12 / distanceFrontOfCamera));

        graphics.drawImage(
                transformedTexture,
                (int) (topLeftPos.getX() / BOX_RADIUS * HALF_WIDTH + HALF_WIDTH),
                (int) (topLeftPos.getY() / BOX_RADIUS * HALF_HEIGHT + HALF_HEIGHT)// - (newDimensions / 2),
                        - (newDimensions - texture.getHeight()),
                newDimensions,
                newDimensions,
                null
        );
    }

    /**
     * Handle an entity at a single point (write its texture on the canvas) with its given coordinates.
     * @param entities The list of entities to compare the coordinates to.
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The Z coordinate of the block.
     */
    private void handleEntities(List<Entity> entities, double x, double y, double z) {
        for (Entity entity : entities) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;
            Location playerLocation = player.getLocation();
            if (playerLocation.getBlockX() != (int) x
                    || playerLocation.getBlockY() != (int) y
                    || playerLocation.getBlockZ() != (int) z) continue;

            double preciseX = playerLocation.getX();
            double preciseY = playerLocation.getY();
            double preciseZ = playerLocation.getZ();

            double[] distances = getDistances(preciseX, preciseY, preciseZ);

            if (distances == null) return;

            double distanceFrontOfCamera = distances[0];
            double distanceSideOfCamera = distances[1];

            float yawToCamera = 0;
            switch (facing) {
                case NORTH:
                    yawToCamera = playerLocation.getYaw();
                    break;
                case EAST:
                    yawToCamera = playerLocation.getYaw() - 90;
                    break;
                case SOUTH:
                    yawToCamera = playerLocation.getYaw() + 180;
                    break;
                case WEST:
                    yawToCamera = playerLocation.getYaw() + 90;
                    break;

            }

            boolean isToFront = (yawToCamera < 90) || (yawToCamera > 270);

            BufferedImage combinedTexture = isToFront ? VideoUtil.getPlayerSkinFront(player) : VideoUtil.getPlayerSkinBack(player);

            Point2D topLeftPos = get2D(
                    (int) distanceSideOfCamera,
                    (int) (location.getY() - y),
                    (int) distanceFrontOfCamera
            );

            BufferedImage transformedTexture = new BufferedImage(
                    combinedTexture.getWidth() * 3,
                    combinedTexture.getHeight() * 3,
                    BufferedImage.TYPE_INT_ARGB
            );
            transformedTexture.createGraphics().drawImage(
                    combinedTexture,
                    combinedTexture.getWidth(),
                    combinedTexture.getHeight(),
                    null
            );

            AffineTransform tx = new AffineTransform();
            tx.shear(0, -distanceSideOfCamera / 10);

            AffineTransformOp op = new AffineTransformOp(tx,
                    AffineTransformOp.TYPE_BILINEAR);
            transformedTexture = op.filter(transformedTexture, null);

            int newDimensions = (int) Math.abs(Math.floor(16 * 16 / distanceFrontOfCamera));

            float yawToSides = yawToCamera > 180
                    ? -(yawToCamera - 360)
                    : yawToCamera;

            yawToSides = Math.abs(yawToSides - 90);

            double dimensionX = Math.abs((newDimensions * 0.5) * (yawToSides) / 180) + (newDimensions * 0.5);

            graphics.drawImage(
                    transformedTexture,
                    (int) ((topLeftPos.getX() / BOX_RADIUS * HALF_WIDTH + HALF_WIDTH)
                            - ((dimensionX / 2) * 0.5 + (dimensionX * (distanceFrontOfCamera / BOX_RADIUS)) * 0.2)),
                    (int) (topLeftPos.getY() / BOX_RADIUS * HALF_HEIGHT * 1.1 + HALF_HEIGHT)
                            - (int) (distanceFrontOfCamera * 2) + (newDimensions / 3),
                    (int) dimensionX,
                    -newDimensions,
                    null
            );
        }
    }

    /**
     * Create a 2D point on the canvas from 3-dimensional block coordinates.
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The Z coordinate of the block.
     * @return The 2D point where the block should be on the canvas.
     */
    private Point2D get2D(int x, int y, int z) {
        return new Point2D.Double(
                (((double) x / z) / 16) * HALF_WIDTH,
                -(((double) y / z) / 16) * HALF_HEIGHT
        );
    }

    /**
     * Get the distances a coordinate is from the camera.
     * @param x The x value of the coordinates.
     * @param y The y value of the coordinates.
     * @param z The z value of the coordinates.
     * @return A 2-element double array with the distance to the front of the camera,
     * and the distance to the side.
     */
    private double[] getDistances(double x, double y, double z) {
        double distanceFrontOfCamera = 0;
        double distanceSideOfCamera = 0;
        if (this.facing.getModX() != 0) {
            if (this.facing.getModX() > 0 && x < location.getX()) {
                return null;
            }
            if (this.facing.getModX() < 0 && x > location.getX()) {
                return null;
            }
            distanceFrontOfCamera = Math.abs(location.getX() - x);
            distanceSideOfCamera = (location.getZ() - z) * -negativeTest(this.facing.getModZ()) - 1 - this.facing.getModX();
        }
        if (this.facing.getModZ() != 0) {
            if (this.facing.getModZ() > 0 && z < location.getZ()) {
                return null;
            }
            if (this.facing.getModZ() < 0 && z > location.getZ()) {
                return null;
            }
            distanceFrontOfCamera = Math.abs(location.getZ() - z);
            distanceSideOfCamera = (location.getX() - x) * -negativeTest(this.facing.getModX()) + this.facing.getModZ();
        }
        return new double[]{distanceFrontOfCamera, distanceSideOfCamera};
    }

    private double negativeTest(double number) {
        if (number == 0) return 1;
        return number;
    }

}
