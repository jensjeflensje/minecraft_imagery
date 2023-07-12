package dev.jensderuiter.minecraft_imagery.skript.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.jensderuiter.minecraft_imagery.image.ImageCaptureOptions;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Image from storage")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprImageCaptureOptions extends SimpleExpression<ImageCaptureOptions> {

    static {
        Skript.registerExpression(
                ExprImageCaptureOptions.class,
                ImageCaptureOptions.class,
                ExpressionType.SIMPLE,
                "capture options [fov %-float%] [day light cycle aware %-boolean%]"
        );
    }

    private Expression<Float> fov;
    private Expression<Boolean> dayLightCycleAware	;

    public Class<? extends ImageCaptureOptions> getReturnType() {
        return ImageCaptureOptions.class;
    }
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        fov = (Expression<Float>) e[0];
        dayLightCycleAware	 = (Expression<Boolean>) e[1];
        return true;
    }
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "image (from|of) %storedimage%";
    }
    @Nullable
    protected ImageCaptureOptions[] get(Event event) {
        ImageCaptureOptions.ImageCaptureOptionsBuilder options = ImageCaptureOptions.builder();
        if (fov != null) options.fov(fov.getSingle(event));
        if (dayLightCycleAware	 != null) options.dayLightCycleAware(dayLightCycleAware.getSingle(event));
        return new ImageCaptureOptions[]{options.build()};
    }
}