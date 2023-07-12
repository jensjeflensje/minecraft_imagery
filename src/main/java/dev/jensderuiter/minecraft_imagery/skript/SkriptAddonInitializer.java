package dev.jensderuiter.minecraft_imagery.skript;

import ch.njol.skript.Skript;
import dev.jensderuiter.minecraft_imagery.ImageryAPIPlugin;

import java.io.IOException;

/**
 * Used to register the Skript addon.
 * Should only be called when Skript is actually installed.
 */
public class SkriptAddonInitializer {

    public ch.njol.skript.SkriptAddon addon;

    public SkriptAddonInitializer(ImageryAPIPlugin plugin) {
        try {
            addon = Skript.registerAddon(plugin)
                    .loadClasses("dev.jensderuiter.minecraft_imagery.skript", "addon")
                    .setLanguageFileDirectory("lang");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
