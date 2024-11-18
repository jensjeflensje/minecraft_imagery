package dev.jensderuiter.minecraft_imagery;

import dev.jensderuiter.minecraft_imagery.image.TranslucentBlock;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Constants for image captures.
 * This class contains the default options for image capture options.
 */
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

            Material material;
            try {
                material = Material.valueOf(blockKey.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // may not exist when on a different Minecraft version
                continue;
            }

            BLOCKS.put(material, new Color(
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
            Material.SHORT_GRASS
    );

    // TODO: make this not contain a duplicate material entry
    @SuppressWarnings("deprecation") // it's no biggie if we support depricated keys like LEGACY_STAINED_GLASS_PANE here
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
        put(Material.SPRUCE_BUTTON, new Color(114,84,48));

        put(Material.BLACK_CARPET, new Color(20,21,25));
        put(Material.WHITE_CARPET, new Color(233,236,236));
        put(Material.BLUE_CARPET, new Color(53,57,157));
        put(Material.BROWN_CARPET, new Color(114,71,40));
        put(Material.CYAN_CARPET, new Color(21,137,145));
        put(Material.GRAY_CARPET, new Color(62,68,71));
        put(Material.GREEN_CARPET, new Color(84,109,27));
        put(Material.LIGHT_BLUE_CARPET, new Color(58,175,217));
        put(Material.LIGHT_GRAY_CARPET, new Color(142,142,134));
        put(Material.LIME_CARPET, new Color(112,185,25));
        put(Material.MAGENTA_CARPET, new Color(189,68,179));
        put(Material.ORANGE_CARPET, new Color(240,118,19));
        put(Material.PINK_CARPET, new Color(237,141,172));
        put(Material.PURPLE_CARPET, new Color(121,42,172));
        put(Material.RED_CARPET, new Color(160,39,34));
        put(Material.YELLOW_CARPET, new Color(248,197,39));
    }};
}
