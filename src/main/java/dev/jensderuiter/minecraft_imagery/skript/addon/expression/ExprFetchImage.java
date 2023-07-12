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
import dev.jensderuiter.minecraft_imagery.skript.addon.type.StoredImage;
import dev.jensderuiter.minecraft_imagery.storage.StorageException;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

@Name("Image from storage")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprFetchImage extends SimpleExpression<StoredImage> {

    static {
        Skript.registerExpression(
                ExprFetchImage.class,
                StoredImage.class,
                ExpressionType.SIMPLE,
                "[image] from storage with uuid %string%"
        );
    }

    private Expression<String> uuid;

    public Class<? extends StoredImage> getReturnType() {
        return StoredImage.class;
    }
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        uuid = (Expression<String>) e[0];
        return true;
    }
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "[image] from storage with uuid %string%";
    }
    @Nullable
    protected StoredImage[] get(Event event) {
        String input = uuid.getSingle(event);
        if (input == null) {
            Skript.error("Input for storage fetch cannot be null.");
            return null;
        }

        UUID storageUUID;
        try {
            storageUUID = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            Skript.error("Input for storage fetch must be a uuid.");
            return null;
        }

        try {
            return new StoredImage[]{
                    new StoredImage(storageUUID, ImageryAPIPlugin.storage.fetch(storageUUID))};
        } catch (StorageException e) {
            Skript.error(
                    "Storage exception occurred whilst fetching an image from storage: "
                            + Util.stackTraceToString(e)
            );
            return null;
        }
    }
}