package dev.jensderuiter.minecraft_imagery.compatibility.patch;

import dev.jensderuiter.minecraft_imagery.Constants;
import dev.jensderuiter.minecraft_imagery.compatibility.VersionPatch;
import org.bukkit.Material;

import java.awt.*;

@VersionPatch(version = 19)
public class Patch19 implements Patch {

    @Override
    public void apply() {
        Constants.BLOCKS_OVERRIDES.put(Material.MANGROVE_BUTTON, new Color(117,54,48));
    }

}
