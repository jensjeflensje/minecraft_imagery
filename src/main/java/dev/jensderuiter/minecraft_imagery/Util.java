package dev.jensderuiter.minecraft_imagery;

import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Util {

    public static HashMap<String, BufferedImage> imageCache = new HashMap<>();

    /**
     * Gets an image using the specified name.
     * @param name The name of a block.
     * @return An image if found inside the resources/textures/ folder, otherwise null.
     */
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

    /**
     * Puts all front skin parts together to form a front-facing view of the player skin.
     * Also caches the skin view indefinitely in memory.
     * @param player The player to get the front skin view from
     * @return An image containing the front-facing view of a skin in 16x32 pixels.
     */
    public static BufferedImage getPlayerSkinFront(Player player) {
        BufferedImage image = Util.imageCache.get("skin-front:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = getPlayerSkin(player);

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

    /**
     * Puts all backward-facing skin parts together to form a backwards-facing view of the player skin.
     * Also caches the skin view indefinitely in memory.
     * @param player The player to get the backwards-facing skin view from
     * @return An image containing the backwards-facing view of a skin in 16x32 pixels.
     */
    public static BufferedImage getPlayerSkinBack(Player player) {
        BufferedImage image = Util.imageCache.get("skin-back:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = getPlayerSkin(player);

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

    /**
     * Gets the raw skin texture from the Mojang API.
     * Also caches the skin texture indefinitely in memory.
     * @param player The player to get the skin texture from
     * @return An image containing the raw
     */
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
}
