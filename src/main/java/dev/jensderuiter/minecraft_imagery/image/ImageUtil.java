package dev.jensderuiter.minecraft_imagery.image;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.Util;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {


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

        if (redColor > 255) redColor = 255;
        if (greenColor > 255) greenColor = 255;
        if (blueColor > 255) blueColor = 255;
        return new Color(redColor, greenColor, blueColor);
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

        BufferedImage image = Util.getImage(block.getType().getKey().getKey());
        if (image != null) {
            Bukkit.getLogger().info(String.format(
                    "Missing color entry for %s while it has an image to sample from. " +
                            "Consider rerunning the generatedefaults command.",
                    block.getType()));
        }
        return null;
    }
}
