package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Constants {

    public static int MAP_WIDTH = 128;
    public static int MAP_HEIGHT = 128;

    public static Color SKY_COLOR = new Color(201, 233, 246);

    public static List<Material> EXCLUDED_BLOCKS = Arrays.asList(
            Material.TALL_GRASS,
            Material.GRASS
    );

    public static HashMap<Material, SeeThroughBlock> THROUGH_BLOCKS = new HashMap<>() {{
        put(Material.IRON_BARS, new SeeThroughBlock(Material.IRON_BARS, new double[] {0.8, 0.8, 0.8}));

        // negative values because a cobweb is white
        put(Material.COBWEB, new SeeThroughBlock(Material.IRON_BARS, new double[] {-0.5, -0.5, -0.5}, -1f));

        // glass panes
        put(Material.GLASS_PANE, new SeeThroughBlock(Material.GLASS_PANE, new double[] {0.3, 0.3, 0.3}));
        put(Material.BLACK_STAINED_GLASS_PANE, new SeeThroughBlock(Material.BLACK_STAINED_GLASS_PANE, new double[] {0.9, 0.9, 0.9}));
        put(Material.BLUE_STAINED_GLASS_PANE, new SeeThroughBlock(Material.BLUE_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.9}));
        put(Material.CYAN_STAINED_GLASS_PANE, new SeeThroughBlock(Material.CYAN_STAINED_GLASS_PANE, new double[] {0.3, 0.5, 0.8}));
        put(Material.BROWN_STAINED_GLASS_PANE, new SeeThroughBlock(Material.BROWN_STAINED_GLASS_PANE, new double[] {0.5, 0.4, 0.35}));
        put(Material.GREEN_STAINED_GLASS_PANE, new SeeThroughBlock(Material.GREEN_STAINED_GLASS_PANE, new double[] {0.3, 0.9, 0.3}));
        put(Material.GRAY_STAINED_GLASS_PANE, new SeeThroughBlock(Material.GRAY_STAINED_GLASS_PANE, new double[] {0.5, 0.5, 0.5}));
        put(Material.LIGHT_BLUE_STAINED_GLASS_PANE, new SeeThroughBlock(Material.LIGHT_BLUE_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.7}));
        put(Material.LIME_STAINED_GLASS_PANE, new SeeThroughBlock(Material.LIME_STAINED_GLASS_PANE, new double[] {0.4, 0.9, 0.4}));
        put(Material.MAGENTA_STAINED_GLASS_PANE, new SeeThroughBlock(Material.MAGENTA_STAINED_GLASS_PANE, new double[] {0.8, 0.3, 0.8}));
        put(Material.ORANGE_STAINED_GLASS_PANE, new SeeThroughBlock(Material.ORANGE_STAINED_GLASS_PANE, new double[] {0.8, 0.5, 0.3}));
        put(Material.PINK_STAINED_GLASS_PANE, new SeeThroughBlock(Material.PINK_STAINED_GLASS_PANE, new double[] {0.9, 0.3, 0.9}));
        put(Material.PURPLE_STAINED_GLASS_PANE, new SeeThroughBlock(Material.PURPLE_STAINED_GLASS_PANE, new double[] {0.7, 0.5, 0.6}));
        put(Material.RED_STAINED_GLASS_PANE, new SeeThroughBlock(Material.RED_STAINED_GLASS_PANE, new double[] {0.9, 0.3, 0.3}));
        put(Material.WHITE_STAINED_GLASS_PANE, new SeeThroughBlock(Material.WHITE_STAINED_GLASS_PANE, new double[] {0.2, 0.2, 0.2}));
        put(Material.YELLOW_STAINED_GLASS_PANE, new SeeThroughBlock(Material.YELLOW_STAINED_GLASS_PANE, new double[] {0.9, 0.9, 0.3}));
        put(Material.LIGHT_GRAY_STAINED_GLASS_PANE, new SeeThroughBlock(Material.LIGHT_GRAY_STAINED_GLASS_PANE, new double[] {0.4, 0.4, 0.4}));
        put(Material.LEGACY_STAINED_GLASS_PANE, new SeeThroughBlock(Material.LEGACY_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.3}));

        // glass blocks
        put(Material.GLASS, new SeeThroughBlock(Material.GLASS, new double[] {0.3, 0.3, 0.3}));
        put(Material.BLACK_STAINED_GLASS, new SeeThroughBlock(Material.BLACK_STAINED_GLASS, new double[] {0.9, 0.9, 0.9}));
        put(Material.BLUE_STAINED_GLASS, new SeeThroughBlock(Material.BLUE_STAINED_GLASS, new double[] {0.3, 0.3, 0.9}));
        put(Material.CYAN_STAINED_GLASS, new SeeThroughBlock(Material.CYAN_STAINED_GLASS, new double[] {0.3, 0.5, 0.8}));
        put(Material.BROWN_STAINED_GLASS, new SeeThroughBlock(Material.BROWN_STAINED_GLASS, new double[] {0.5, 0.4, 0.35}));
        put(Material.GREEN_STAINED_GLASS, new SeeThroughBlock(Material.GREEN_STAINED_GLASS, new double[] {0.3, 0.9, 0.3}));
        put(Material.GRAY_STAINED_GLASS, new SeeThroughBlock(Material.GRAY_STAINED_GLASS, new double[] {0.5, 0.5, 0.5}));
        put(Material.LIGHT_BLUE_STAINED_GLASS, new SeeThroughBlock(Material.LIGHT_BLUE_STAINED_GLASS, new double[] {0.3, 0.3, 0.7}));
        put(Material.LIME_STAINED_GLASS, new SeeThroughBlock(Material.LIME_STAINED_GLASS, new double[] {0.4, 0.9, 0.4}));
        put(Material.MAGENTA_STAINED_GLASS, new SeeThroughBlock(Material.MAGENTA_STAINED_GLASS, new double[] {0.8, 0.3, 0.8}));
        put(Material.ORANGE_STAINED_GLASS, new SeeThroughBlock(Material.ORANGE_STAINED_GLASS, new double[] {0.8, 0.5, 0.3}));
        put(Material.PINK_STAINED_GLASS, new SeeThroughBlock(Material.PINK_STAINED_GLASS, new double[] {0.9, 0.3, 0.9}));
        put(Material.PURPLE_STAINED_GLASS, new SeeThroughBlock(Material.PURPLE_STAINED_GLASS, new double[] {0.7, 0.5, 0.6}));
        put(Material.RED_STAINED_GLASS, new SeeThroughBlock(Material.RED_STAINED_GLASS, new double[] {0.9, 0.3, 0.3}));
        put(Material.WHITE_STAINED_GLASS, new SeeThroughBlock(Material.WHITE_STAINED_GLASS, new double[] {0.2, 0.2, 0.2}));
        put(Material.YELLOW_STAINED_GLASS, new SeeThroughBlock(Material.YELLOW_STAINED_GLASS, new double[] {0.9, 0.9, 0.3}));
        put(Material.LIGHT_GRAY_STAINED_GLASS, new SeeThroughBlock(Material.LIGHT_GRAY_STAINED_GLASS, new double[] {0.4, 0.4, 0.4}));
        put(Material.LEGACY_STAINED_GLASS, new SeeThroughBlock(Material.LEGACY_STAINED_GLASS, new double[] {0.3, 0.3, 0.3}));
    }};
}
