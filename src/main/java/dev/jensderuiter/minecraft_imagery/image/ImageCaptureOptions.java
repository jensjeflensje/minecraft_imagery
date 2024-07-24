package dev.jensderuiter.minecraft_imagery.image;

import dev.jensderuiter.minecraft_imagery.Constants;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

@Getter
@Builder
public class ImageCaptureOptions {

    @Builder.Default
    private int width = Constants.MAP_WIDTH;

    @Builder.Default
    private int height = Constants.MAP_HEIGHT;

    // Should be a reasonable value near 1
    // Lower values are more zoomed in, higher values have a wider view
    // Examples would be 0.5 or 2
    @Builder.Default
    private float fov = 1;

    @Builder.Default
    private Color skyColor = Constants.SKY_COLOR;

    @Builder.Default
    private Color skyColorNight = Constants.SKY_COLOR_NIGHT;

    @Builder.Default
    private List<Material> excludedBlocks = Constants.EXCLUDED_BLOCKS;

    @Builder.Default
    private HashMap<Material, TranslucentBlock> translucentBlocks = Constants.TRANSLUCENT_BLOCKS;

    @Builder.Default
    private HashMap<Material, Color> blocks = Constants.BLOCKS;

    @Builder.Default
    private HashMap<Material, Color> blocksOverrides = Constants.BLOCKS_OVERRIDES;

    @Builder.Default
    private boolean dayLightCycleAware = true;

    @Builder.Default
    private boolean showDepth = true;

}
