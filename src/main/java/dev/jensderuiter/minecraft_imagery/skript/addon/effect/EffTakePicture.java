package dev.jensderuiter.minecraft_imagery.skript.addon.effect;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.util.AsyncEffect;
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


@Name("Remove an image")
@Description("Remove an image from storage.")
public class EffTakePicture extends AsyncEffect {

    static {
        Skript.registerEffect(
                EffTakePicture.class,
                "take picture from %location% [with options %-imagecaptureconfig%] [with players %players%] and set to %-object%"
        );
    }

    private Expression<Location> location;
    private Expression<ImageCaptureOptions> options;
    private Expression<Player> players;

    // The variable that we're going to put the image in
    private Expression<Variable> variable;

    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        location = (Expression<Location>) e[0];
        options = (Expression<ImageCaptureOptions>) e[1];
        players = (Expression<Player>) e[2];
        variable = (Expression<Variable>) e[3];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "take picture from %location% [with options %-imagecaptureconfig%] [with players %players%] and set to %-variable%";
    }

    @Override
    protected void execute(Event event) {
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
            return;
        }
        variable.change(
                event,
                new StoredImage[] {new StoredImage(uuid, image)},
                Changer.ChangeMode.SET
        );
    }

}