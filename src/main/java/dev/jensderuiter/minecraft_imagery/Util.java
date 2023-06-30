package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Util {

    // VideoFrameCapture

    private static HashMap<String, BufferedImage> imageCache = new HashMap<>();

    private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    public static BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static BufferedImage getImage(String name) {
        BufferedImage image = imageCache.get("block:" + name);
        if (image != null) return image;

        InputStream imageStream = ImageryAPIPlugin.plugin.getResource("textures/" + name + ".png");
        if (imageStream == null) return null;

        try {
            image = ImageIO.read(imageStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageCache.put("block:" + name, image);
        return image;
    }

    public static BufferedImage getPlayerSkinFront(Player player) {
        BufferedImage image = imageCache.get("skin-front:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = Util.getPlayerSkin(player);

        int armSize = 4;
        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.SLIM) {
            armSize = 3;
        }

        BufferedImage head = skin.getSubimage(8, 8, 8, 8);
        BufferedImage body = skin.getSubimage(20, 20, 8, 12);
        BufferedImage leftArm = skin.getSubimage(52 - 5 * armSize, 52, 4, 12);
        BufferedImage rightArm = skin.getSubimage(40 + armSize, 20, 4, 12);
        BufferedImage leftLeg = skin.getSubimage(4, 20, 4, 12);
        BufferedImage rightLeg = skin.getSubimage(20, 52, 4, 12);

        BufferedImage combinedTexture = new BufferedImage(
                16,
                32,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D combinedTextureGraphics = combinedTexture.createGraphics();
        combinedTextureGraphics.drawImage(head, 4, 0, 8, 8, null);
        combinedTextureGraphics.drawImage(body, 4, 8, 8, 12, null);
        combinedTextureGraphics.drawImage(leftArm, 4 - armSize, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(rightArm, 12, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(leftLeg, 4, 20, 4, 12, null);
        combinedTextureGraphics.drawImage(rightLeg, 8, 20, 4, 12, null);

        combinedTextureGraphics.dispose();

        imageCache.put("skin-front:" + player.getName(), combinedTexture);

        return combinedTexture;
    }

    public static float addDegrees(float original, float toAdd) {
        float newNumber = original + toAdd;
        if (newNumber > 360) {
            newNumber = toAdd - original * -1;
        }
        return newNumber;
    }

    public static BufferedImage getPlayerSkinBack(Player player) {
        BufferedImage image = imageCache.get("skin-back:" + player.getName());
        if (image != null) return image;

        BufferedImage skin = Util.getPlayerSkin(player);

        int armSize = 4;
        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.SLIM) {
            armSize = 3;
        }

        BufferedImage head = skin.getSubimage(24, 8, 8, 8);
        BufferedImage body = skin.getSubimage(28, 20, 8, 12);
        BufferedImage leftArm = skin.getSubimage(52 - 5 * armSize, 52, 4, 12);
        BufferedImage rightArm = skin.getSubimage(40 + armSize, 20, 4, 12);
        BufferedImage leftLeg = skin.getSubimage(4, 20, 4, 12);
        BufferedImage rightLeg = skin.getSubimage(20, 52, 4, 12);

        BufferedImage combinedTexture = new BufferedImage(
                16,
                32,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D combinedTextureGraphics = combinedTexture.createGraphics();
        combinedTextureGraphics.drawImage(head, 4, 0, 8, 8, null);
        combinedTextureGraphics.drawImage(body, 4, 8, 8, 12, null);
        combinedTextureGraphics.drawImage(leftArm, 4 - armSize, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(rightArm, 12, 8, armSize, 12, null);
        combinedTextureGraphics.drawImage(leftLeg, 4, 20, 4, 12, null);
        combinedTextureGraphics.drawImage(rightLeg, 8, 20, 4, 12, null);

        combinedTextureGraphics.dispose();

        imageCache.put("skin-back:" + player.getName(), combinedTexture);

        return combinedTexture;
    }


    private static BufferedImage getPlayerSkin(Player player) {
        BufferedImage image = imageCache.get("skin:" + player.getName());
        if (image != null) return image;

        try {
            image = ImageIO.read(player.getPlayerProfile().getTextures().getSkin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageCache.put("skin:" + player.getName(), image );
        return image;
    }



    // ImageCapture

    static Map<Material, Color> blocksMap = new HashMap<Material, Color>();

    public static void loadColors() {
        blocksMap.put(Material.COBBLESTONE, new Color(130, 130, 130));
        blocksMap.put(Material.COBBLESTONE_STAIRS, new Color(130, 130, 130));
        blocksMap.put(Material.COBBLESTONE_SLAB, new Color(130, 130, 130));
        blocksMap.put(Material.SMOOTH_STONE_SLAB, new Color(154, 154, 154));
        blocksMap.put(Material.STONE_BRICK_STAIRS, new Color(136, 134, 134));
        blocksMap.put(Material.STONE_BRICK_SLAB, new Color(136, 134, 134));
        blocksMap.put(Material.BRICKS, new Color(133, 84, 72));
        blocksMap.put(Material.BRICK_SLAB, new Color(133, 84, 72));
        blocksMap.put(Material.BRICK_STAIRS, new Color(133, 84, 72));
        blocksMap.put(Material.FURNACE, new Color(130, 130, 130));
        blocksMap.put(Material.STONE, new Color(117, 117, 117));
        blocksMap.put(Material.COBBLESTONE_WALL, new Color(92, 91, 91));
        blocksMap.put(Material.STONE_SLAB, new Color(117, 117, 117));
        blocksMap.put(Material.IRON_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.GOLD_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.REDSTONE_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.DIAMOND_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.COAL_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.EMERALD_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.LAPIS_ORE, new Color(117, 117, 117));
        blocksMap.put(Material.IRON_BLOCK, new Color(236, 236, 236));
        blocksMap.put(Material.GOLD_BLOCK, new Color(243, 223, 75));
        blocksMap.put(Material.REDSTONE_BLOCK, new Color(196, 25, 16));
        blocksMap.put(Material.DIAMOND_BLOCK, new Color(95, 233, 217));
        blocksMap.put(Material.COAL_BLOCK, new Color(19, 19, 19));
        blocksMap.put(Material.EMERALD_BLOCK, new Color(71, 213, 105));
        blocksMap.put(Material.LAPIS_BLOCK, new Color(42, 80, 139));
        blocksMap.put(Material.WATER, new Color(67, 101, 165));
        blocksMap.put(Material.SEAGRASS, new Color(67, 101, 165));
        blocksMap.put(Material.BUBBLE_COLUMN, new Color(67, 101, 165));
        blocksMap.put(Material.TALL_SEAGRASS, new Color(67, 101, 165));
        blocksMap.put(Material.KELP, new Color(67, 101, 165));
        blocksMap.put(Material.GRASS_BLOCK, new Color(82, 129, 69));
        blocksMap.put(Material.DIRT, new Color(168, 120, 83));
        blocksMap.put(Material.SAND, new Color(222, 215, 172));
        blocksMap.put(Material.SANDSTONE, new Color(213, 207, 162));
        blocksMap.put(Material.ACACIA_LEAVES, new Color(44, 97, 22));
        blocksMap.put(Material.BIRCH_LEAVES, new Color(114, 149, 76));
        blocksMap.put(Material.DARK_OAK_LEAVES, new Color(46, 111, 17));
        blocksMap.put(Material.JUNGLE_LEAVES, new Color(60, 141, 24));
        blocksMap.put(Material.OAK_LEAVES, new Color(49, 111, 21));
        blocksMap.put(Material.SPRUCE_LEAVES, new Color(55, 91, 56));
        blocksMap.put(Material.COARSE_DIRT, new Color(104, 75, 51));
        blocksMap.put(Material.PODZOL, new Color(101, 71, 47));
        blocksMap.put(Material.ANDESITE, new Color(136, 136, 138));
        blocksMap.put(Material.DIORITE, new Color(181, 181, 181));
        blocksMap.put(Material.DEAD_BUSH, new Color(144, 97, 39));
        blocksMap.put(Material.CACTUS, new Color(76, 107, 35));
        blocksMap.put(Material.DANDELION, new Color(247, 229, 77));
        blocksMap.put(Material.POPPY, new Color(230, 47, 43));
        blocksMap.put(Material.CORNFLOWER, new Color(70, 106, 235));
        blocksMap.put(Material.AZURE_BLUET, new Color(210, 215, 223));
        blocksMap.put(Material.OXEYE_DAISY, new Color(187, 188, 189));
        blocksMap.put(Material.LAVA, new Color(211, 124, 40));
        blocksMap.put(Material.GRANITE, new Color(156, 111, 91));
        blocksMap.put(Material.REDSTONE_LAMP, new Color(123, 73, 33));
        blocksMap.put(Material.GRAVEL, new Color(139, 135, 134));
        blocksMap.put(Material.SPRUCE_LOG, new Color(48, 34, 25));
        blocksMap.put(Material.OAK_LOG, new Color(58, 35, 9));
        blocksMap.put(Material.OAK_WOOD, new Color(58, 35, 9));
        blocksMap.put(Material.BIRCH_LOG, new Color(196, 195, 193));
        blocksMap.put(Material.BIRCH_WOOD, new Color(196, 195, 193));
        blocksMap.put(Material.JUNGLE_LOG, new Color(89, 76, 37));
        blocksMap.put(Material.JUNGLE_WOOD, new Color(89, 76, 37));
        blocksMap.put(Material.ACACIA_LOG, new Color(95, 95, 85));
        blocksMap.put(Material.ACACIA_WOOD, new Color(95, 95, 85));
        blocksMap.put(Material.DARK_OAK_LOG, new Color(35, 27, 16));
        blocksMap.put(Material.DARK_OAK_WOOD, new Color(35, 27, 16));
        blocksMap.put(Material.SPRUCE_PLANKS, new Color(100, 78, 47));
        blocksMap.put(Material.OAK_PLANKS, new Color(172, 140, 88));
        blocksMap.put(Material.OAK_WALL_SIGN, new Color(157, 128, 81));
        blocksMap.put(Material.BIRCH_PLANKS, new Color(202, 185, 131));
        blocksMap.put(Material.JUNGLE_PLANKS, new Color(172, 124, 89));
        blocksMap.put(Material.ACACIA_PLANKS, new Color(178, 102, 60));
        blocksMap.put(Material.DARK_OAK_PLANKS, new Color(62, 41, 18));
        blocksMap.put(Material.SPRUCE_FENCE, new Color(100, 78, 47));
        blocksMap.put(Material.CHEST, new Color(107, 84, 53));
        blocksMap.put(Material.OAK_FENCE, new Color(172, 140, 88));
        blocksMap.put(Material.BIRCH_FENCE, new Color(202, 185, 131));
        blocksMap.put(Material.JUNGLE_FENCE, new Color(172, 124, 89));
        blocksMap.put(Material.ACACIA_FENCE, new Color(178, 102, 60));
        blocksMap.put(Material.DARK_OAK_FENCE, new Color(62, 41, 18));
        blocksMap.put(Material.SPRUCE_STAIRS, new Color(100, 78, 47));
        blocksMap.put(Material.OAK_STAIRS, new Color(172, 140, 88));
        blocksMap.put(Material.BIRCH_STAIRS, new Color(202, 185, 131));
        blocksMap.put(Material.JUNGLE_STAIRS, new Color(172, 124, 89));
        blocksMap.put(Material.ACACIA_STAIRS, new Color(178, 102, 60));
        blocksMap.put(Material.DARK_OAK_STAIRS, new Color(62, 41, 18));
        blocksMap.put(Material.SPRUCE_SLAB, new Color(100, 78, 47));
        blocksMap.put(Material.OAK_SLAB, new Color(172, 140, 88));
        blocksMap.put(Material.BIRCH_SLAB, new Color(202, 185, 131));
        blocksMap.put(Material.JUNGLE_SLAB, new Color(172, 124, 89));
        blocksMap.put(Material.ACACIA_SLAB, new Color(178, 102, 60));
        blocksMap.put(Material.DARK_OAK_SLAB, new Color(62, 41, 18));
        blocksMap.put(Material.CRAFTING_TABLE, new Color(172, 140, 88));
        blocksMap.put(Material.BOOKSHELF, new Color(172, 140, 88));
        blocksMap.put(Material.SUGAR_CANE, new Color(71, 139, 42));
        blocksMap.put(Material.BEDROCK, new Color(47, 47, 47));
        blocksMap.put(Material.TORCH, new Color(206, 173, 26));
        blocksMap.put(Material.WALL_TORCH, new Color(206, 173, 26));
        blocksMap.put(Material.PUMPKIN, new Color(222, 141, 28));
        blocksMap.put(Material.CARVED_PUMPKIN, new Color(222, 141, 28));
        blocksMap.put(Material.JACK_O_LANTERN, new Color(222, 141, 28));
        blocksMap.put(Material.TNT, new Color(203, 49, 26));
        blocksMap.put(Material.BLACK_WOOL, new Color(6, 7, 12));
        blocksMap.put(Material.WHITE_WOOL, new Color(225, 226, 228));
        blocksMap.put(Material.BLUE_WOOL, new Color(45, 50, 145));
        blocksMap.put(Material.BROWN_WOOL, new Color(105, 70, 39));
        blocksMap.put(Material.CYAN_WOOL, new Color(21, 139, 145));
        blocksMap.put(Material.GRAY_WOOL, new Color(64, 67, 72));
        blocksMap.put(Material.GREEN_WOOL, new Color(83, 108, 20));
        blocksMap.put(Material.LIGHT_BLUE_WOOL, new Color(121, 148, 202));
        blocksMap.put(Material.LIGHT_GRAY_WOOL, new Color(164, 168, 169));
        blocksMap.put(Material.LIME_WOOL, new Color(122, 198, 38));
        blocksMap.put(Material.MAGENTA_WOOL, new Color(188, 66, 179));
        blocksMap.put(Material.ORANGE_WOOL, new Color(240, 125, 30));
        blocksMap.put(Material.PINK_WOOL, new Color(242, 148, 177));
        blocksMap.put(Material.PURPLE_WOOL, new Color(129, 65, 182));
        blocksMap.put(Material.RED_WOOL, new Color(155, 53, 49));
        blocksMap.put(Material.YELLOW_WOOL, new Color(195, 182, 47));
        blocksMap.put(Material.BLACK_BANNER, new Color(6, 7, 12));
        blocksMap.put(Material.WHITE_BANNER, new Color(225, 226, 228));
        blocksMap.put(Material.BLUE_BANNER, new Color(45, 50, 145));
        blocksMap.put(Material.BROWN_BANNER, new Color(105, 70, 39));
        blocksMap.put(Material.CYAN_BANNER, new Color(21, 139, 145));
        blocksMap.put(Material.GRAY_BANNER, new Color(64, 67, 72));
        blocksMap.put(Material.GREEN_BANNER, new Color(83, 108, 20));
        blocksMap.put(Material.LIGHT_BLUE_BANNER, new Color(121, 148, 202));
        blocksMap.put(Material.LIGHT_GRAY_BANNER, new Color(164, 168, 169));
        blocksMap.put(Material.LIME_BANNER, new Color(122, 198, 38));
        blocksMap.put(Material.MAGENTA_BANNER, new Color(188, 66, 179));
        blocksMap.put(Material.ORANGE_BANNER, new Color(240, 125, 30));
        blocksMap.put(Material.PINK_BANNER, new Color(242, 148, 177));
        blocksMap.put(Material.PURPLE_BANNER, new Color(129, 65, 182));
        blocksMap.put(Material.RED_BANNER, new Color(155, 53, 49));
        blocksMap.put(Material.YELLOW_BANNER, new Color(195, 182, 47));
        blocksMap.put(Material.BLACK_WALL_BANNER, new Color(6, 7, 12));
        blocksMap.put(Material.WHITE_WALL_BANNER, new Color(225, 226, 228));
        blocksMap.put(Material.BLUE_WALL_BANNER, new Color(45, 50, 145));
        blocksMap.put(Material.BROWN_WALL_BANNER, new Color(105, 70, 39));
        blocksMap.put(Material.CYAN_WALL_BANNER, new Color(21, 139, 145));
        blocksMap.put(Material.GRAY_WALL_BANNER, new Color(64, 67, 72));
        blocksMap.put(Material.GREEN_WALL_BANNER, new Color(83, 108, 20));
        blocksMap.put(Material.LIGHT_BLUE_WALL_BANNER, new Color(121, 148, 202));
        blocksMap.put(Material.LIGHT_GRAY_WALL_BANNER, new Color(164, 168, 169));
        blocksMap.put(Material.LIME_WALL_BANNER, new Color(122, 198, 38));
        blocksMap.put(Material.MAGENTA_WALL_BANNER, new Color(188, 66, 179));
        blocksMap.put(Material.ORANGE_WALL_BANNER, new Color(240, 125, 30));
        blocksMap.put(Material.PINK_WALL_BANNER, new Color(242, 148, 177));
        blocksMap.put(Material.PURPLE_WALL_BANNER, new Color(129, 65, 182));
        blocksMap.put(Material.RED_WALL_BANNER, new Color(155, 53, 49));
        blocksMap.put(Material.YELLOW_WALL_BANNER, new Color(195, 182, 47));
        blocksMap.put(Material.BLACK_CONCRETE, new Color(7, 9, 14));
        blocksMap.put(Material.WHITE_CONCRETE, new Color(199, 202, 207));
        blocksMap.put(Material.BLUE_CONCRETE, new Color(42, 44, 133));
        blocksMap.put(Material.BROWN_CONCRETE, new Color(91, 57, 30));
        blocksMap.put(Material.CYAN_CONCRETE, new Color(20, 113, 129));
        blocksMap.put(Material.GRAY_CONCRETE, new Color(118, 119, 110));
        blocksMap.put(Material.GREEN_CONCRETE, new Color(88, 156, 25));
        blocksMap.put(Material.LIGHT_BLUE_CONCRETE, new Color(33, 130, 190));
        blocksMap.put(Material.LIGHT_GRAY_CONCRETE, new Color(111, 115, 116));
        blocksMap.put(Material.LIME_CONCRETE, new Color(90, 162, 23));
        blocksMap.put(Material.MAGENTA_CONCRETE, new Color(162, 47, 152));
        blocksMap.put(Material.ORANGE_CONCRETE, new Color(207, 81, 1));
        blocksMap.put(Material.PINK_CONCRETE, new Color(205, 95, 138));
        blocksMap.put(Material.PURPLE_CONCRETE, new Color(155, 45, 145));
        blocksMap.put(Material.RED_CONCRETE, new Color(136, 30, 33));
        blocksMap.put(Material.YELLOW_CONCRETE, new Color(222, 162, 19));
        blocksMap.put(Material.SNOW, new Color(232, 240, 239));
        blocksMap.put(Material.SNOW_BLOCK, new Color(232, 240, 239));
        blocksMap.put(Material.GLASS, new Color(255, 255, 255));
        blocksMap.put(Material.WHITE_STAINED_GLASS, new Color(255, 255, 255));
        blocksMap.put(Material.GLASS_PANE, new Color(255, 255, 255));
        blocksMap.put(Material.WHITE_STAINED_GLASS_PANE, new Color(255, 255, 255));
        blocksMap.put(Material.CAMPFIRE, new Color(206, 173, 26));
        blocksMap.put(Material.COMMAND_BLOCK, new Color(198, 126, 78));
    }

    public static boolean hasColor(Block block) {
        return colorFromType(block, new double[] {0, 0, 0}) != null;
    }

    public static double[] applyToDye(double[] currentDye, double[] dyeToApply, float factor) {
        double[] newDye = currentDye.clone();
        // dye color intensity
        float intensity = 0.5f;
        newDye[0] -= (dyeToApply[1] * dyeToApply[2]) * factor * intensity;
        newDye[1] -= (dyeToApply[0] * dyeToApply[2]) * factor * intensity;
        newDye[2] -= (dyeToApply[0] * dyeToApply[1]) * factor * intensity;

        if (newDye[0] < 0) newDye[0] = 0;
        if (newDye[1] < 0) newDye[1] = 0;
        if (newDye[2] < 0) newDye[2] = 0;

        return newDye;
    }

    public static Color applyDye(Color color, double[] dye) {
        int redColor = (int) (color.getRed() * dye[0]);
        int greenColor = (int) (color.getGreen() * dye[1]);
        int blueColor = (int) (color.getBlue() * dye[2]);

        if(redColor > 255) redColor = 255;
        if(greenColor > 255) greenColor = 255;
        if(blueColor > 255) blueColor = 255;
        return new Color(redColor, greenColor, blueColor);
    }

    public static Color seekColor(Location location, double[] dye, Vector rayTraceVector) {
        Color color = null;
        Block block = location.getBlock();
        if (!Constants.EXCLUDED_BLOCKS.contains(block.getType())) {
            color = Util.colorFromType(location.getBlock(), dye);
        }

        int i = 0;
        while (color == null) {
            if (i > 40) {
                break;
            }

            location.add(0, -i, 0);

//            RayTraceResult result = location.getWorld().rayTraceBlocks(
//                    location, rayTraceVector, i);

            block = location.getBlock();

            if (!Constants.EXCLUDED_BLOCKS.contains(block.getType())) {
                color = Util.colorFromType(
                        block,
                        dye
                );
            }

            i++;
        }
        return color != null ? color : Constants.SKY_COLOR;
    }

    public static Color colorFromType(Block block, double[] dye) {
        if (blocksMap.containsKey(block.getType())) {
            // if blockMap has a color for the material, use that color
            return applyDye(blocksMap.get(block.getType()), dye);
        }

        if (Constants.EXCLUDED_BLOCKS.contains(block.getType())) return Constants.SKY_COLOR;

        BufferedImage image = getImage(block.getType().getKey().getKey());
        if (image != null) {
            // gets certain pixel in image to use as color,
            // so we don't need to access the image multiple times.
            Color color = new Color(image.getRGB((int) (image.getWidth() / 1.5), (int) (image.getHeight() / 1.5)));

            blocksMap.put(block.getType(), color);
            return applyDye(color, dye);
        } else {
            Bukkit.getLogger().info("Missing Image For: " + block.getType());
        }
        return null;
    }

    public static boolean isWithinBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static boolean isWithinBlockIgnoreY(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }
}
