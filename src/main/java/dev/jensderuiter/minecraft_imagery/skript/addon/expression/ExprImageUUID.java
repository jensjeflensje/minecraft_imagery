package dev.jensderuiter.minecraft_imagery.skript.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.jensderuiter.minecraft_imagery.skript.addon.type.StoredImage;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Image from storage")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprImageUUID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprImageUUID.class,
                String.class,
                ExpressionType.SIMPLE,
                "uuid (from|of) %storedimage%"
        );
    }

    private Expression<StoredImage> storedImage;

    public Class<? extends String> getReturnType() {
        return String.class;
    }
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        storedImage = (Expression<StoredImage>) e[0];
        return true;
    }
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "uuid (from|of) %storedimage%";
    }
    @Nullable
    protected String[] get(Event event) {
        return new String[]{storedImage.getSingle(event).getUuid().toString()};
    }
}