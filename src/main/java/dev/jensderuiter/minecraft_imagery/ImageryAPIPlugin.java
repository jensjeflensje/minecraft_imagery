package dev.jensderuiter.minecraft_imagery;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ImageryAPIPlugin extends JavaPlugin {

    public static ImageryAPIPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Util.loadColors();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
