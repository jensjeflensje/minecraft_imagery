package dev.jensderuiter.minecraft_imagery.storage;

import dev.jensderuiter.minecraft_imagery.ImageryAPIPlugin;
import org.bukkit.Bukkit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

// TODO: javadocs
public class LocalStorageProvider implements StorageProvider {

    private File folder;

    public LocalStorageProvider() {
        this.folder = Paths.get(
                ImageryAPIPlugin.plugin.getDataFolder().toString(), "storage").toFile();

        if (!this.folder.exists() && !this.folder.mkdirs())
            throw new StorageRuntimeException("Local directory creation failed");

        int fileCount = this.folder.list().length;

        Bukkit.getLogger().info(String.format(
                "Local storage successfully initialized (currently containing %d entries)", fileCount));
    }

    @Override
    public BufferedImage fetch(UUID uuid) throws StorageException {
        String fileName = getFileName(uuid.toString());
        File file = new File(fileName);
        if (!file.exists()) return null;
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new StorageException(
                    String.format(
                            "Fetching %s failed: %s",
                            fileName,
                            e.getMessage()
                    )
            );
        }
    }

    @Override
    public UUID store(BufferedImage image) throws StorageException {
        while (true) {
            UUID uuid = UUID.randomUUID();
            String fileName = getFileName(uuid.toString());
            File file = new File(fileName);
            if (file.exists()) continue;
            try {
                if (!ImageIO.write(image, getExtension(), file)) {
                    throw new StorageException(
                            String.format(
                                    "Writing %s failed: could not write",
                                    fileName
                            )
                    );
                }
                return uuid;
            } catch (IOException e) {
                throw new StorageException(
                        String.format(
                                "Writing %s failed: %s",
                                fileName,
                                e.getMessage()
                        )
                );
            }
        }
    }

    @Override
    public void remove(UUID uuid) throws StorageException {
        String fileName = getFileName(uuid.toString());
        File file = new File(fileName);
        if (file.delete()) return;
        throw new StorageException(
                String.format(
                        "Removing %s",
                        fileName
                )
        );
    }

    @Override
    public String getFileName(String fileName) {
        return Paths.get(this.folder.toString(), fileName + "." + getExtension()).toString();
    }

}
