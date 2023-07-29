package dev.jensderuiter.minecraft_imagery.compatibility.patch;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.compatibility.VersionPatch;
import dev.jensderuiter.minecraft_imagery.image.TranslucentBlock;
import org.bukkit.Material;

@VersionPatch(version = 17)
public class Patch17 implements Patch {

    @Override
    public void apply() {
        Constants.TRANSLUCENT_BLOCKS.put(Material.TINTED_GLASS, new TranslucentBlock(Material.TINTED_GLASS, new double[] {0.7, 0.7, 0.7}));
    }

}
