package dev.jensderuiter.minecraft_imagery;

import org.bukkit.Location;

import java.awt.geom.Point2D;

public class PlayerOccurrence2D {

    public Point2D point;
    public Location location;

    public PlayerOccurrence2D(Point2D point, Location location) {
        this.point = point;
        this.location = location;
    }

}
