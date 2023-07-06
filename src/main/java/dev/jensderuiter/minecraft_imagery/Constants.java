package dev.jensderuiter.minecraft_imagery;

import dev.jensderuiter.minecraft_imagery.image.TranslucentBlock;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Constants {

    protected static void init() {
        initBlocks();
    }

    /**
     * Clears in-memory constant cache.
     * Currently only used for solid blocks.
     */
    protected static void clear() {
        BLOCKS.clear();
    }

    /**
     * Fetches all material-bound color codes and puts them in the solid block map.
     */
    private static void initBlocks() {
        ConfigurationSection blocksSection =
                ImageryAPIPlugin.plugin.getConfig().getConfigurationSection("blocks");

        for (String blockKey : blocksSection.getKeys(false)) {
            String[] colorParts = blocksSection.getString(blockKey).split(",");

            BLOCKS.put(Material.valueOf(blockKey.toUpperCase()), new Color(
                    Integer.parseInt(colorParts[0]),
                    Integer.parseInt(colorParts[1]),
                    Integer.parseInt(colorParts[2])
            ));
        }
    }

    public static int MAP_WIDTH = 128;
    public static int MAP_HEIGHT = 128;

    public static Color SKY_COLOR = new Color(201, 233, 246);
    public static Color SKY_COLOR_NIGHT = new Color(68, 59, 65);

    // the camera will look through these blocks
    public static List<Material> EXCLUDED_BLOCKS = Arrays.asList(
            Material.TALL_GRASS,
            Material.FERN,
            Material.LARGE_FERN,
            Material.GRASS
    );

    // TODO: make this not contain a duplicate material entry
    public static HashMap<Material, TranslucentBlock> TRANSLUCENT_BLOCKS = new HashMap<>() {{
        put(Material.IRON_BARS, new TranslucentBlock(Material.IRON_BARS, new double[] {0.8, 0.8, 0.8}));

        // negative values because a cobweb is white
        put(Material.COBWEB, new TranslucentBlock(Material.IRON_BARS, new double[] {-0.5, -0.5, -0.5}, -3f));

        put(Material.DEAD_BUSH, new TranslucentBlock(Material.DEAD_BUSH, new double[] {0.65, 0.4, 0.3}, 2f));
        put(Material.VINE, new TranslucentBlock(Material.DEAD_BUSH, new double[] {0.3, 0.6, 0.3}, 2f));

        // glass panes
        put(Material.GLASS_PANE, new TranslucentBlock(Material.GLASS_PANE, new double[] {0.3, 0.3, 0.3}));
        put(Material.BLACK_STAINED_GLASS_PANE, new TranslucentBlock(Material.BLACK_STAINED_GLASS_PANE, new double[] {0.9, 0.9, 0.9}));
        put(Material.BLUE_STAINED_GLASS_PANE, new TranslucentBlock(Material.BLUE_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.9}));
        put(Material.CYAN_STAINED_GLASS_PANE, new TranslucentBlock(Material.CYAN_STAINED_GLASS_PANE, new double[] {0.3, 0.5, 0.8}));
        put(Material.BROWN_STAINED_GLASS_PANE, new TranslucentBlock(Material.BROWN_STAINED_GLASS_PANE, new double[] {0.5, 0.4, 0.35}));
        put(Material.GREEN_STAINED_GLASS_PANE, new TranslucentBlock(Material.GREEN_STAINED_GLASS_PANE, new double[] {0.3, 0.9, 0.3}));
        put(Material.GRAY_STAINED_GLASS_PANE, new TranslucentBlock(Material.GRAY_STAINED_GLASS_PANE, new double[] {0.5, 0.5, 0.5}));
        put(Material.LIGHT_BLUE_STAINED_GLASS_PANE, new TranslucentBlock(Material.LIGHT_BLUE_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.7}));
        put(Material.LIME_STAINED_GLASS_PANE, new TranslucentBlock(Material.LIME_STAINED_GLASS_PANE, new double[] {0.4, 0.9, 0.4}));
        put(Material.MAGENTA_STAINED_GLASS_PANE, new TranslucentBlock(Material.MAGENTA_STAINED_GLASS_PANE, new double[] {0.8, 0.3, 0.8}));
        put(Material.ORANGE_STAINED_GLASS_PANE, new TranslucentBlock(Material.ORANGE_STAINED_GLASS_PANE, new double[] {0.8, 0.5, 0.3}));
        put(Material.PINK_STAINED_GLASS_PANE, new TranslucentBlock(Material.PINK_STAINED_GLASS_PANE, new double[] {0.9, 0.3, 0.9}));
        put(Material.PURPLE_STAINED_GLASS_PANE, new TranslucentBlock(Material.PURPLE_STAINED_GLASS_PANE, new double[] {0.7, 0.5, 0.6}));
        put(Material.RED_STAINED_GLASS_PANE, new TranslucentBlock(Material.RED_STAINED_GLASS_PANE, new double[] {0.9, 0.3, 0.3}));
        put(Material.WHITE_STAINED_GLASS_PANE, new TranslucentBlock(Material.WHITE_STAINED_GLASS_PANE, new double[] {0.2, 0.2, 0.2}));
        put(Material.YELLOW_STAINED_GLASS_PANE, new TranslucentBlock(Material.YELLOW_STAINED_GLASS_PANE, new double[] {0.9, 0.9, 0.3}));
        put(Material.LIGHT_GRAY_STAINED_GLASS_PANE, new TranslucentBlock(Material.LIGHT_GRAY_STAINED_GLASS_PANE, new double[] {0.4, 0.4, 0.4}));
        put(Material.LEGACY_STAINED_GLASS_PANE, new TranslucentBlock(Material.LEGACY_STAINED_GLASS_PANE, new double[] {0.3, 0.3, 0.3}));

        // glass blocks
        put(Material.GLASS, new TranslucentBlock(Material.GLASS, new double[] {0.3, 0.3, 0.3}));
        put(Material.TINTED_GLASS, new TranslucentBlock(Material.TINTED_GLASS, new double[] {0.7, 0.7, 0.7}));
        put(Material.BLACK_STAINED_GLASS, new TranslucentBlock(Material.BLACK_STAINED_GLASS, new double[] {0.9, 0.9, 0.9}));
        put(Material.BLUE_STAINED_GLASS, new TranslucentBlock(Material.BLUE_STAINED_GLASS, new double[] {0.3, 0.3, 0.9}));
        put(Material.CYAN_STAINED_GLASS, new TranslucentBlock(Material.CYAN_STAINED_GLASS, new double[] {0.3, 0.5, 0.8}));
        put(Material.BROWN_STAINED_GLASS, new TranslucentBlock(Material.BROWN_STAINED_GLASS, new double[] {0.5, 0.4, 0.35}));
        put(Material.GREEN_STAINED_GLASS, new TranslucentBlock(Material.GREEN_STAINED_GLASS, new double[] {0.3, 0.9, 0.3}));
        put(Material.GRAY_STAINED_GLASS, new TranslucentBlock(Material.GRAY_STAINED_GLASS, new double[] {0.5, 0.5, 0.5}));
        put(Material.LIGHT_BLUE_STAINED_GLASS, new TranslucentBlock(Material.LIGHT_BLUE_STAINED_GLASS, new double[] {0.3, 0.3, 0.7}));
        put(Material.LIME_STAINED_GLASS, new TranslucentBlock(Material.LIME_STAINED_GLASS, new double[] {0.4, 0.9, 0.4}));
        put(Material.MAGENTA_STAINED_GLASS, new TranslucentBlock(Material.MAGENTA_STAINED_GLASS, new double[] {0.8, 0.3, 0.8}));
        put(Material.ORANGE_STAINED_GLASS, new TranslucentBlock(Material.ORANGE_STAINED_GLASS, new double[] {0.8, 0.5, 0.3}));
        put(Material.PINK_STAINED_GLASS, new TranslucentBlock(Material.PINK_STAINED_GLASS, new double[] {0.9, 0.3, 0.9}));
        put(Material.PURPLE_STAINED_GLASS, new TranslucentBlock(Material.PURPLE_STAINED_GLASS, new double[] {0.7, 0.5, 0.6}));
        put(Material.RED_STAINED_GLASS, new TranslucentBlock(Material.RED_STAINED_GLASS, new double[] {0.9, 0.3, 0.3}));
        put(Material.WHITE_STAINED_GLASS, new TranslucentBlock(Material.WHITE_STAINED_GLASS, new double[] {0.2, 0.2, 0.2}));
        put(Material.YELLOW_STAINED_GLASS, new TranslucentBlock(Material.YELLOW_STAINED_GLASS, new double[] {0.9, 0.9, 0.3}));
        put(Material.LIGHT_GRAY_STAINED_GLASS, new TranslucentBlock(Material.LIGHT_GRAY_STAINED_GLASS, new double[] {0.4, 0.4, 0.4}));
        put(Material.LEGACY_STAINED_GLASS, new TranslucentBlock(Material.LEGACY_STAINED_GLASS, new double[] {0.3, 0.3, 0.3}));

        put(Material.BARRIER, new TranslucentBlock(Material.BARRIER, new double[] {0, 0, 0}));
    }};

    public static HashMap<Material, Color> BLOCKS = new HashMap<>(); // all solid blocks

    // overrides cover cases where the average color of a texture is not quite right
    public static HashMap<Material, Color> BLOCKS_OVERRIDES = new HashMap<>() {{
        put(Material.ACACIA_LEAVES, new Color(44, 97, 22));
        put(Material.BIRCH_LEAVES, new Color(114, 149, 76));
        put(Material.DARK_OAK_LEAVES, new Color(46, 111, 17));
        put(Material.JUNGLE_LEAVES, new Color(60, 141, 24));
        put(Material.OAK_LEAVES, new Color(49, 111, 21));

        put(Material.GRASS_BLOCK, new Color(104, 159, 83));

        put(Material.LILY_PAD, new Color(46, 91, 26));

        put(Material.COBBLESTONE_SLAB, new Color(110, 110, 110));
        put(Material.COBBLESTONE_STAIRS, new Color(110, 110, 110));


        put(Material.STONE_BUTTON, new Color(120, 120, 120));
        put(Material.POLISHED_BLACKSTONE_BUTTON, new Color(40, 40, 40));
        put(Material.BIRCH_BUTTON, new Color(196,195,193));
        put(Material.ACACIA_BUTTON, new Color(95,95,85));
        put(Material.JUNGLE_BUTTON, new Color(89,76,37));
        put(Material.OAK_BUTTON, new Color(58,35,9));
        put(Material.DARK_OAK_BUTTON, new Color(35,27,16));
        put(Material.WARPED_BUTTON, new Color(43,104,99));
        put(Material.CRIMSON_BUTTON, new Color(101,48,70));
        put(Material.MANGROVE_BUTTON, new Color(117,54,48));
        put(Material.SPRUCE_BUTTON, new Color(114,84,48));
    }};
}
