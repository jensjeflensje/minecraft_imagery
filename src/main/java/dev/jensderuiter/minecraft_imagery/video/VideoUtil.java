package dev.jensderuiter.minecraft_imagery.video;

import dev.jensderuiter.minecraft_imagery.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class VideoUtil {

    public static BufferedImage getPlayerSkinFront(Player player) {
        BufferedImage image = Util.imageCache.get("skin-front:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = VideoUtil.getPlayerSkin(player);

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

        Util.imageCache.put("skin-front:" + player.getName(), combinedTexture);

        return combinedTexture;
    }

    public static BufferedImage getPlayerSkinBack(Player player) {
        BufferedImage image = Util.imageCache.get("skin-back:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = VideoUtil.getPlayerSkin(player);

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

        Util.imageCache.put("skin-back:" + player.getName(), combinedTexture);

        return combinedTexture;
    }


    private static BufferedImage getPlayerSkin(Player player) {
        BufferedImage image = Util.imageCache.get("skin:" + player.getName());
        if (image != null) return image;

        try {
            image = ImageIO.read(player.getPlayerProfile().getTextures().getSkin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Util.imageCache.put("skin:" + player.getName(), image );
        return image;
    }

    public static boolean isWithinBlockIgnoreY(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }
}
