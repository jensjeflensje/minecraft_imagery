package dev.jensderuiter.minecraft_imagery;

import dev.jensderuiter.minecraft_imagery.command.ManagementCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ImageryAPIPlugin extends JavaPlugin {

    public static ImageryAPIPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getCommand("imageryapi").setExecutor(new ManagementCommand());

        saveDefaultConfig();

        Constants.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void reloadConstantsConfig() {
        Constants.clear();
        Constants.init();
    }

}
