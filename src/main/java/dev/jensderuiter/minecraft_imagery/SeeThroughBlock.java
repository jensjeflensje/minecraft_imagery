package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Material;


public class SeeThroughBlock {

    // this dye will be used to alter the color of the end result
    public double[] dye;

    public Material material;

    // may be used to make the dye work the other way around (make the subject lighter)
    public float factor;

    public SeeThroughBlock(Material material, double[] dye, float factor) {
        this.dye = dye;
        this.material = material;
        this.factor = factor;
    }

    public SeeThroughBlock(Material material, double[] dye) {
        this(material, dye, 1f);
    }

}
