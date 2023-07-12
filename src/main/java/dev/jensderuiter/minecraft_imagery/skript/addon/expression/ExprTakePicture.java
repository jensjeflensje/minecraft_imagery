package dev.jensderuiter.minecraft_imagery.skript.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.jensderuiter.minecraft_imagery.ImageryAPIPlugin;
import dev.jensderuiter.minecraft_imagery.Util;
import dev.jensderuiter.minecraft_imagery.image.ImageCapture;
import dev.jensderuiter.minecraft_imagery.image.ImageCaptureOptions;
import dev.jensderuiter.minecraft_imagery.skript.addon.type.StoredImage;
import dev.jensderuiter.minecraft_imagery.storage.StorageException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.UUID;

@Name("Take a picture from a location")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprTakePicture extends SimpleExpression<StoredImage> {

    static {
        Skript.registerExpression(
                ExprTakePicture.class,
                StoredImage.class,
                ExpressionType.SIMPLE,
                "[image] taken from %location% [with options %-imagecaptureconfig%] [with players %players%]"
        );
    }

    private Expression<Location> location;
    private Expression<ImageCaptureOptions> options;
    private Expression<Player> players;

    public Class<? extends StoredImage> getReturnType() {
        return StoredImage.class;
    }

    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        location = (Expression<Location>) e[0];
        options = (Expression<ImageCaptureOptions>) e[1];
        players = (Expression<Player>) e[2];
        return true;
    }

    public String toString(@Nullable Event arg0, boolean arg1) {
        return "[image] from storage with uuid %string% [with options %-imagecaptureconfig%] [with players %players%]";
    }

    @Nullable
    protected StoredImage[] get(Event event) {
        ImageCaptureOptions finalOptions = options != null
                ? options.getSingle(event)
                : ImageCaptureOptions.builder().build();

        ImageCapture capture = new ImageCapture(
                location.getSingle(event),
                Arrays.stream(players.getAll(event)).toList(),
                finalOptions
        );
        BufferedImage image = capture.render();
        UUID uuid;
        try {
            uuid = ImageryAPIPlugin.storage.store(image);
        } catch (StorageException e) {
            Skript.error(
                    "Storage exception occurred whilst storing an image: "
                            + Util.stackTraceToString(e)
            );
            return null;
        }
        return new StoredImage[] {new StoredImage(uuid, image)};
    }
}