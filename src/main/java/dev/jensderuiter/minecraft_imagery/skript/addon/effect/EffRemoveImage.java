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
import dev.jensderuiter.minecraft_imagery.skript.addon.type.StoredImage;
import dev.jensderuiter.minecraft_imagery.storage.StorageException;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


@Name("Remove an image")
@Description("Remove an image from storage.")
public class EffRemoveImage extends AsyncEffect {

    static {
        Skript.registerEffect(
                EffRemoveImage.class,
                "remove image %storedimage% from storage"
        );
    }

    private Expression<StoredImage> image;

    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        image = (Expression<StoredImage>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "remove image %storedimage% from storage";
    }

    @Override
    protected void execute(Event event) {
        try {
            ImageryAPIPlugin.storage.remove(image.getSingle(event).getUuid());
        } catch (StorageException e) {
            Skript.error(
                    "Storage exception occurred whilst removing an image from storage: "
                            + Util.stackTraceToString(e)
            );
        }
    }

}