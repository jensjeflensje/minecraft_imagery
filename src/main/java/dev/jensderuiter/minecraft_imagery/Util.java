package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class Util {

    // VideoFrameCapture

    private static HashMap<String, BufferedImage> imageCache = new HashMap<>();

    private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    public static BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static BufferedImage getImage(String name) {
        BufferedImage image = imageCache.get("block:" + name);
        if (image != null) return image;

        InputStream imageStream = ImageryAPIPlugin.plugin.getResource("textures/" + name + ".png");
        if (imageStream == null) return null;

        try {
            image = ImageIO.read(imageStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageCache.put("block:" + name, image);
        return image;
    }

    public static BufferedImage getPlayerSkinFront(Player player) {
        BufferedImage image = imageCache.get("skin-front:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = Util.getPlayerSkin(player);

        int armSize = 4;
        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.SLIM) {
            armSize = 3;
        }

        BufferedImage head = skin.getSubimage(8, 8, 8, 8);
        BufferedImage body = skin.getSubimage(20, 20, 8, 12);
        BufferedImage leftArm = skin.getSubimage(52 - 5 * armSize, 52, 4, 12);
        BufferedImage rightArm = skin.getSubimage(40 + armSize, 20, 4, 12);
        BufferedImage leftLeg = skin.getSubimage(4, 20, 4, 12);
        BufferedImage rightLeg = skin.getSubimage(20, 52, 4, 12);

        BufferedImage combinedTexture = new BufferedImage(
                16,
                32,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D combinedTextureGraphics = combinedTexture.createGraphics();
        combinedTextureGraphics.drawImage(head, 4, 0, 8, 8, null);
        combinedTextureGraphics.drawImage(body, 4, 8, 8, 12, null);
        combinedTextureGraphics.drawImage(leftArm, 4 - armSize, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(rightArm, 12, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(leftLeg, 4, 20, 4, 12, null);
        combinedTextureGraphics.drawImage(rightLeg, 8, 20, 4, 12, null);

        combinedTextureGraphics.dispose();

        imageCache.put("skin-front:" + player.getName(), combinedTexture);

        return combinedTexture;
    }

    public static float addDegrees(float original, float toAdd) {
        float newNumber = original + toAdd;
        if (newNumber > 360) {
            newNumber = toAdd - original * -1;
        }
        return newNumber;
    }

    public static BufferedImage getPlayerSkinBack(Player player) {
        BufferedImage image = imageCache.get("skin-back:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = Util.getPlayerSkin(player);

        int armSize = 4;
        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.SLIM) {
            armSize = 3;
        }

        BufferedImage head = skin.getSubimage(24, 8, 8, 8);
        BufferedImage body = skin.getSubimage(28, 20, 8, 12);
        BufferedImage leftArm = skin.getSubimage(52 - 5 * armSize, 52, 4, 12);
        BufferedImage rightArm = skin.getSubimage(40 + armSize, 20, 4, 12);
        BufferedImage leftLeg = skin.getSubimage(4, 20, 4, 12);
        BufferedImage rightLeg = skin.getSubimage(20, 52, 4, 12);

        BufferedImage combinedTexture = new BufferedImage(
                16,
                32,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D combinedTextureGraphics = combinedTexture.createGraphics();
        combinedTextureGraphics.drawImage(head, 4, 0, 8, 8, null);
        combinedTextureGraphics.drawImage(body, 4, 8, 8, 12, null);
        combinedTextureGraphics.drawImage(leftArm, 4 - armSize, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(rightArm, 12, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(leftLeg, 4, 20, 4, 12, null);
        combinedTextureGraphics.drawImage(rightLeg, 8, 20, 4, 12, null);

        combinedTextureGraphics.dispose();

        imageCache.put("skin-back:" + player.getName(), combinedTexture);

        return combinedTexture;
    }


    private static BufferedImage getPlayerSkin(Player player) {
        BufferedImage image = imageCache.get("skin:" + player.getName());
        if (image != null) return image;

        try {
            image = ImageIO.read(player.getPlayerProfile().getTextures().getSkin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageCache.put("skin:" + player.getName(), image );
        return image;
    }


    public static double[] applyToDye(double[] currentDye, double[] dyeToApply, float factor) {
        double[] newDye = currentDye.clone();
        // dye color intensity
        float intensity = 0.5f;
        newDye[0] -= (dyeToApply[1] * dyeToApply[2]) * factor * intensity;
        newDye[1] -= (dyeToApply[0] * dyeToApply[2]) * factor * intensity;
        newDye[2] -= (dyeToApply[0] * dyeToApply[1]) * factor * intensity;

        if (newDye[0] < 0) newDye[0] = 0;
        if (newDye[1] < 0) newDye[1] = 0;
        if (newDye[2] < 0) newDye[2] = 0;

        return newDye;
    }

    public static Color applyDye(Color color, double[] dye) {
        int redColor = (int) (color.getRed() * dye[0]);
        int greenColor = (int) (color.getGreen() * dye[1]);
        int blueColor = (int) (color.getBlue() * dye[2]);

        if(redColor > 255) redColor = 255;
        if(greenColor > 255) greenColor = 255;
        if(blueColor > 255) blueColor = 255;
        return new Color(redColor, greenColor, blueColor);
    }

    public static Color seekColor(Location location, double[] dye, Vector rayTraceVector) {
        Color color = null;
        Block block = location.getBlock();
        if (!Constants.EXCLUDED_BLOCKS.contains(block.getType())) {
            color = Util.colorFromType(location.getBlock(), dye);
        }

        int i = 0;
        while (color == null) {
            if (i > 40) {
                break;
            }

            location.add(0, -i, 0);
            block = location.getBlock();

            if (!Constants.EXCLUDED_BLOCKS.contains(block.getType())) {
                color = Util.colorFromType(
                        block,
                        dye
                );
            }

            i++;
        }
        return color != null ? color : Constants.SKY_COLOR;
    }

    // https://stackoverflow.com/questions/28162488/get-average-color-on-bufferedimage-and-bufferedimage-portion-as-fast-as-possible
    public static Color getColorFromImage(BufferedImage bi) {
        long sumr = 0, sumg = 0, sumb = 0;
        int pixels = 0;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                int rgb = bi.getRGB(x, y);
                if (rgb == 0) continue;
                Color pixel = new Color(rgb);
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
                pixels++;
            }
        }
        return new Color(
                Math.round(sumr / pixels),
                Math.round(sumg / pixels),
                Math.round(sumb / pixels)
        );
    }

    public static Color colorFromType(Block block, double[] dye) {
        if (Constants.BLOCKS.containsKey(block.getType())) {
            // if blockMap has a color for the material, use that color
            return applyDye(Constants.BLOCKS.get(block.getType()), dye);
        }

        if (Constants.EXCLUDED_BLOCKS.contains(block.getType())) return Constants.SKY_COLOR;

        BufferedImage image = getImage(block.getType().getKey().getKey());
        if (image != null) {
            // gets certain pixel in image to use as color,
            // so we don't need to access the image multiple times.
            Color color = getColorFromImage(image);

            Constants.BLOCKS.put(block.getType(), color);
            return applyDye(color, dye);
        } else {
            Bukkit.getLogger().info("Missing Image For: " + block.getType());
        }
        return null;
    }

    public static boolean isWithinBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static boolean isWithinBlockIgnoreY(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }
}
