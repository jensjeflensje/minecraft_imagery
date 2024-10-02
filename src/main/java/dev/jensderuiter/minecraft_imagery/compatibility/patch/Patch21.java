package dev.jensderuiter.minecraft_imagery.compatibility.patch;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.compatibility.VersionPatch;
import org.bukkit.Material;

import java.awt.*;


@VersionPatch(version = 21)
public class Patch21 implements Patch {

    @Override
    public void apply() {
        // add map colors as fallback for any material that doesn't have a color in config
        for (Material material : Material.values()) {
            if (Constants.BLOCKS.containsKey(material)) return;

            Constants.BLOCKS.put(material, new Color(material.createBlockData().getMapColor().asRGB()));
        }
    }

}
