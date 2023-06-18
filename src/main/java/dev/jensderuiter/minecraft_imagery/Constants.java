package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Constants {

    public static int MAP_WIDTH = 128;
    public static int MAP_HEIGHT = 128;

    public static Color SKY_COLOR = new Color(201, 233, 246);

    public static List<Material> EXCLUDED_BLOCKS = Arrays.asList(
            Material.TALL_GRASS,
            Material.GRASS
    );

    public static List<Material> THROUGH_BLOCKS = Arrays.asList(
            Material.GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.LEGACY_STAINED_GLASS_PANE
    );
}
