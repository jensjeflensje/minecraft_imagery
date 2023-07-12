package dev.jensderuiter.minecraft_imagery.skript.addon.expression;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.ExpressionType;
import dev.jensderuiter.minecraft_imagery.skript.addon.type.StoredImage;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("Image from storage")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprImage extends SimpleExpression<Image> {

    static {
        Skript.registerExpression(
                ExprImage.class,
                Image.class,
                ExpressionType.SIMPLE,
                "image (from|of) %storedimage%"
        );
    }

    private Expression<StoredImage> storedImage;

    public Class<? extends Image> getReturnType() {
        return Image.class;
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
        return "image (from|of) %storedimage%";
    }
    @Nullable
    protected Image[] get(Event event) {
        return new BufferedImage[]{storedImage.getSingle(event).getImage()};
    }
}