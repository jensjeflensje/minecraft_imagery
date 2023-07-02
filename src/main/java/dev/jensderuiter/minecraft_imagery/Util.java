package dev.jensderuiter.minecraft_imagery;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Util {

    public static HashMap<String, BufferedImage> imageCache = new HashMap<>();

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

}
