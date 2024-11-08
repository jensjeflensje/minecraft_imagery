package dev.jensderuiter.minecraft_imagery;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class Util {

    public static HashMap<String, BufferedImage> imageCache = new HashMap<>();

    // true is steve, false is alex (slim)
    public static HashMap<Player, Boolean> skinModelCache = new HashMap<>();

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
        if (!skinModelCache.get(player)) {
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
     * @return An image containing the raw skin
     */
    private static BufferedImage getPlayerSkin(Player player) {
        BufferedImage image = Util.imageCache.get("skin:" + player.getName());
        if (image != null) return image;

        try {
            JsonObject profile = getPlayerProfile(player.getUniqueId());
            String skinUrl = profile
                    .get("textures").getAsJsonObject()
                    .get("SKIN").getAsJsonObject()
                    .get("url").getAsString();
            image = ImageIO.read(URI.create(skinUrl).toURL());

            skinModelCache.put(player, profile
                    .get("textures").getAsJsonObject()
                    .get("SKIN").getAsJsonObject()
                    .get("metadata") == null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Util.imageCache.put("skin:" + player.getName(), image );
        return image;
    }

    /**
     * Gets the player profile from the Mojang API.
     * @param uuid The uuid of the player
     * @return A JSONObject containing the profile
     */
    private static JsonObject getPlayerProfile(UUID uuid) throws IOException {
        URL url = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int status = conn.getResponseCode();
        if (status != 200) throw new RuntimeException("Mojang API request was not successful");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        JsonObject json = new Gson().fromJson(content.toString(), JsonObject.class);

        byte[] decodedBytes = Base64.getDecoder().decode(
                json.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString());
        String profile = new String(decodedBytes, StandardCharsets.UTF_8);

        return new Gson().fromJson(profile, JsonObject.class);
    }

    /**
     * Converts a throwable's stacktrace to a string.
     * Used for further formatting.
     * @param error The throwable to get the stacktrace from.
     * @return The string containing the stacktrace.
     */
    public static String stackTraceToString(Throwable error) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : error.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
