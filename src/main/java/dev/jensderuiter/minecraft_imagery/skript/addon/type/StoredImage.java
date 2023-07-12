package dev.jensderuiter.minecraft_imagery.skript.addon.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * Represents an image that also has a presence inside storage.
 * The UUID is used as a reference to the image file in storage.
 */
@AllArgsConstructor
@Getter
public class StoredImage {

    private UUID uuid;
    private BufferedImage image;

}
