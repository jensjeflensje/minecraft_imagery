package dev.jensderuiter.minecraft_imagery.command;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.ImageryAPIPlugin;
import dev.jensderuiter.minecraft_imagery.Util;
import dev.jensderuiter.minecraft_imagery.image.ImageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ManagementCommand extends AbstractCommand {

    public void sendHelp() {
        this.sender.sendMessage("ImageryAPI HELP");
        this.sender.sendMessage("/imageryapi generatedefaults");
        this.sender.sendMessage("/imageryapi savedefaults");
        this.sender.sendMessage("/imageryapi reloadconfig");
    }

    @Override
    public String getPermission() {
        return "imageryapi.admin";
    }

    @Override
    public void execute() {
        if (args.length == 0) {
            this.sendHelp();
            return;
        }

        switch (args[0]) {
            case "generatedefaults" -> {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ConfigurationSection blocksSection =
                                ImageryAPIPlugin.plugin.getConfig().getConfigurationSection("blocks");
                        for (Material material : Material.values()) {
                            String name = material.getKey().getKey();
                            BufferedImage image = Util.getImage(name);
                            if (image == null) continue;
                            Color color = ImageUtil.getColorFromImage(image);
                            blocksSection.set(name, color.getRed() + "," + color.getGreen() + "," + color.getBlue());
                        }

                        for (Map.Entry<Material, Color> entry : Constants.BLOCKS_OVERRIDES.entrySet()) {
                            Color color = entry.getValue();
                            blocksSection.set(
                                    entry.getKey().getKey().getKey(), // lol
                                    color.getRed() + "," + color.getGreen() + "," + color.getBlue()
                            );
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ImageryAPIPlugin.plugin.saveConfig();
                                sender.sendMessage("Solid blocks done.");
                            }
                        }.runTask(ImageryAPIPlugin.plugin);
                    }
                }.runTaskAsynchronously(ImageryAPIPlugin.plugin);
                sender.sendMessage("Generating every block color entry...");
            }
            case "savedefaults" -> {
                ImageryAPIPlugin.plugin.saveResource("config.yml", true);
                sender.sendMessage("Saved all default config settings.");
            }
            case "reloadconfig" -> {
                ImageryAPIPlugin.reloadConstantsConfig();
                sender.sendMessage("Reloaded config.");
            }
            default -> this.sendHelp();
        }
    }
}
