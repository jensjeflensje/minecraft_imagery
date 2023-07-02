package dev.jensderuiter.minecraft_imagery.image;

import org.bukkit.Material;


public class TranslucentBlock {
    /**
     * Represents a block that should be looked through, but has to alter the resulting color.
     * Examples would be stained-glass or iron bars.
     */

    // this dye will be used to alter the color of the end result
    public double[] dye;

    public Material material;

    // may be used to make the dye work the other way around (make the subject lighter)
    public float factor;

    public TranslucentBlock(Material material, double[] dye, float factor) {
        this.dye = dye;
        this.material = material;
        this.factor = factor;
    }

    public TranslucentBlock(Material material, double[] dye) {
        this(material, dye, 1f);
    }

}
