package dev.jensderuiter.minecraft_imagery;

import dev.jensderuiter.minecraft_imagery.command.ManagementCommand;
import dev.jensderuiter.minecraft_imagery.storage.LocalStorageProvider;
import dev.jensderuiter.minecraft_imagery.storage.S3StorageProvider;
import dev.jensderuiter.minecraft_imagery.storage.StorageProvider;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ImageryAPIPlugin extends JavaPlugin {

    public static ImageryAPIPlugin plugin;
    public static StorageProvider storage;

    private YamlConfiguration storageConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getCommand("imageryapi").setExecutor(new ManagementCommand());

        saveDefaultConfig();

        Constants.init();

        this.initStorage();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Reloads all constants. Currently only solid blocks.
     */
    public static void reloadConstantsConfig() {
        Constants.clear();
        Constants.init();
    }

    // TODO: javadocs
    private void initStorage() {
        this.createCustomConfig();

        switch (this.storageConfig.getString("provider")) {
            case "local":
                storage = new LocalStorageProvider();
                break;
            case "s3":
                storage = new S3StorageProvider(
                        this.storageConfig.getString("s3.endpoint"),
                        this.storageConfig.getString("s3.region"),
                        this.storageConfig.getString("s3.access_key"),
                        this.storageConfig.getString("s3.secret_key"),
                        this.storageConfig.getString("s3.bucket")
                );
                break;
            default:
                storage = null;
                break;
        }
    }

    private void createCustomConfig() {
        File storageConfigFile = new File(getDataFolder(), "storage.yml");
        if (!storageConfigFile.exists()) {
            storageConfigFile.getParentFile().mkdirs();
            saveResource("storage.yml", false);
        }

        storageConfig = new YamlConfiguration();
        try {
            storageConfig.load(storageConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
