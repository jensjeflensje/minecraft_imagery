package dev.jensderuiter.minecraft_imagery.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

import java.awt.geom.Point2D;

@AllArgsConstructor
@Getter
public class RayTracedPoint2D {
    /**
     * Represents a single raytrace results hit position, and its coordinates in a 2D space on the map.
     */

    private Point2D point2D;
    private Location hitPosition;
}
