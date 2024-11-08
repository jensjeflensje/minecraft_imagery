package dev.jensderuiter.minecraft_imagery.skript.addon.effect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import dev.jensderuiter.minecraft_imagery.ImageryAPIPlugin;
import dev.jensderuiter.minecraft_imagery.Util;
import dev.jensderuiter.minecraft_imagery.storage.StorageException;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;


@Name("Remove an image")
@Description("Remove an image from storage using its UUID.")
public class EffRemoveImage extends AsyncEffect {

    static {
        Skript.registerEffect(
                EffRemoveImage.class,
                "remove image %string% from storage"
        );
    }

    private Expression<String> uuid;

    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        uuid = (Expression<String>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "remove image %string% from storage";
    }

    @Override
    protected void execute(Event event) {
        String input = uuid.getSingle(event);
        if (input == null) {
            Skript.error("Input for storage fetch cannot be null.");
            return;
        }

        UUID storageUUID;
        try {
            storageUUID = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            Skript.error("Input for storage fetch must be a uuid.");
            return;
        }

        try {
            ImageryAPIPlugin.storage.remove(storageUUID);
        } catch (StorageException e) {
            Skript.error(
                    "Storage exception occurred whilst removing an image from storage: "
                            + Util.stackTraceToString(e)
            );
        }
    }

}