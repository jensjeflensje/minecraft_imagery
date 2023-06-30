package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Material;


public class SeeThroughBlock {

    // this dye will be used to alter the color of the end result
    public double[] dye;
    public Material material;

    public SeeThroughBlock(Material material, double[] dye) {
        this.dye = dye;
        this.material = material;
    }

}
