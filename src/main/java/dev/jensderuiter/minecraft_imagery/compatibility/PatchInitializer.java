package dev.jensderuiter.minecraft_imagery.compatibility;

import dev.jensderuiter.minecraft_imagery.compatibility.patch.Patch;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PatchInitializer {

    public static void applyPatches() {
        String fullVersion = Bukkit.getBukkitVersion().split("-")[0];
        int minorVersion;
        try {
            minorVersion = Integer.parseInt(fullVersion.split("\\.")[1]);
        } catch (NumberFormatException exception) {
            throw new RuntimeException("Unable to extract server version");
        }

        List<Patch> patches = getAllPatchesForVersion(minorVersion);

        for (Patch patch : patches) patch.apply();
    }

    private static List<Patch> getAllPatchesForVersion(int version) {
        Set<Class<?>> patchClasses = new Reflections("dev.jensderuiter.minecraft_imagery.compatibility.patch")
                .getTypesAnnotatedWith(VersionPatch.class);

        List<Patch> patches = new ArrayList<>();

        for (Class<?> clazz : patchClasses) {
            // cannot be null, as we filtered for classes with that annotation
            VersionPatch annotation = clazz.getAnnotation(VersionPatch.class);

            if (!Patch.class.isAssignableFrom(clazz)) {
                throw new RuntimeException(String.format(
                        "Patch class %s doesn't implement the Patch interface", clazz.getSimpleName()));
            }

            if (version >= annotation.version()) {
                try {
                    patches.add((Patch) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return patches;
    }

}
