package dev.jensderuiter.minecraft_imagery.image;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    /**
     * Subtracts a dye to another dye.
     * It does this by subtracting the multiplied value of the other color planes from a plane.
     * @param currentDye The dye to subtract the dyeToApply from. (3-element array with 0-1 values).
     * @param dyeToApply The dye that will be subtracted from currentDye (3-element array 0-1 values).
     * @param factor A float which will be multiplied by each color plane's end result.
     *               Used to apply intensity on an individual basis.
     * @return The resulting dye (3-element double array).
     */
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

    /**
     * Applies a dye to a color by multiplying each color plane with their dye counterparts.
     * @param color The color to apply the dye to.
     * @param dye The dye that will be multiplied with the color (3-element array).
     * @return The resulting color.
     */
    public static Color applyDye(Color color, double[] dye) {
        int redColor = (int) (color.getRed() * dye[0]);
        int greenColor = (int) (color.getGreen() * dye[1]);
        int blueColor = (int) (color.getBlue() * dye[2]);

        if (redColor > 255) redColor = 255;
        if (greenColor > 255) greenColor = 255;
        if (blueColor > 255) blueColor = 255;
        return new Color(redColor, greenColor, blueColor);
    }

    /**
     * Calculates the average color from an image.
     * Inspired by: https://stackoverflow.com/questions/28162488/get-average-color-on-bufferedimage-and-bufferedimage-portion-as-fast-as-possible.
     * @param bi The image that the color will be sampled from.
     * @return The average color of the image.
     */
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

    /**
     * Gets the color for a block and applies a dye to it.
     * @param block The block to get the color for.
     * @param dye The dye to apply to the color for the block (3-element array).
     * @return The ready-to-use color from the block with the dye applied.
     */
    public static Color colorFromType(Block block, double[] dye) {
        if (Constants.EXCLUDED_BLOCKS.contains(block.getType())) return Constants.SKY_COLOR;

        if (Constants.BLOCKS.containsKey(block.getType())) {
            // if blockMap has a color for the material, use that color
            return applyDye(Constants.BLOCKS.get(block.getType()), dye);
        }

        BufferedImage image = Util.getImage(block.getType().getKey().getKey());
        if (image != null) {
            Bukkit.getLogger().warning(String.format(
                    "Missing color entry for %s while it has an image to sample from. " +
                            "Consider rerunning the generatedefaults command.",
                    block.getType()));
        }
        return null;
    }

    /**
     * Checks if the two locations are inside the same block, ignoring the Y-axis.
     * @param loc1 The location to check against loc2.
     * @param loc2 The location to check against loc1.
     * @return A boolean that only is true when the locations are both in the same X and Z value.
     */
    public static boolean isWithinBlockIgnoreY(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }

    /**
     * Returns a yaw that is always a positive number.
     * The returned value will resemble a 360-degree output.
     * @param rawYaw The yaw gotten from spigot (-180 to 180)
     * @return The transformed yaw (0 to 360).
     */
    public static float positiveYaw(float rawYaw) {
        return rawYaw > 0 ? rawYaw : 180 + (180 + rawYaw);
    }

    /**
     * Returns the difference between two numbers regardless of one or the other being negative.
     * @param val1 The first value.
     * @param val2 The second value.
     * @return The difference between the two values.
     */
    public static double difference(double val1, double val2) {
        return val1 > val2 ? val1 - val2 : val2 - val1;
    }
}
