package dev.jensderuiter.minecraft_imagery.skript.addon.expression;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;

import javax.annotation.Nullable;

@Name("Image from storage")
@Description("Read an image from the storage provider. Supports all storage providers.")
public class ExprMapId extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(
                ExprMapId.class,
                Integer.class,
                ExpressionType.SIMPLE,
                "id of %map%"
        );
    }

    private Expression<MapView> mapView;

    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        mapView = (Expression<MapView>) e[0];
        return true;
    }
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "id of %map%";
    }
    @Nullable
    protected Integer[] get(Event event) {
        return new Integer[] {mapView.getSingle(event).getId()};
    }
}