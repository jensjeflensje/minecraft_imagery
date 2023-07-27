package dev.jensderuiter.minecraft_imagery.storage;

import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * Describes how a storage provider should work.
 */
public interface StorageProvider {

    BufferedImage fetch(UUID uuid) throws StorageException;
    UUID store(BufferedImage image) throws StorageException;
    void remove(UUID uuid) throws StorageException;

    default boolean downloadUrlEnabled() {
        return false;
    }

    default String generateDownloadUrl(UUID uuid) throws StorageException {
        throw new UnsupportedOperationException("Storage provider doesn't implement download url generation");
    }

    default String getExtension() {
        return "png";
    }

    default String getFileName(String name) {
        return name + "." + getExtension();
    }
}
