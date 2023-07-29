package dev.jensderuiter.minecraft_imagery.compatibility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface VersionPatch {

    public int version();

}
