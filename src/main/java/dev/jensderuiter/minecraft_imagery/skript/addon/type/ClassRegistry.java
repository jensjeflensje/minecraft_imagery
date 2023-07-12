package dev.jensderuiter.minecraft_imagery.skript.addon.type;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import dev.jensderuiter.minecraft_imagery.image.ImageCaptureOptions;


/**
 * Used to register classes as Skript types.
 */
public class ClassRegistry {

    static {
        Classes.registerClass(new ClassInfo<>(StoredImage.class, "storedimage")
                .user("storedimages?")
                .name("StoredImage")
                .description("Represents an image inside a storage provider.")
                .parser(new Parser<StoredImage>() {
                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(StoredImage storedImage, int i) {
                        return "StoredImage with UUID: " + storedImage.getUuid();
                    }

                    @Override
                    public String toVariableNameString(StoredImage storedImage) {
                        return this.toString(storedImage, 0);
                    }
                })
        );

        Classes.registerClass(new ClassInfo<>(ImageCaptureOptions.class, "imagecaptureconfig")
                .user("imagecaptureconfigs?")
                .name("ImageCaptureOptions")
                .description("Represents the configuration for an image capture.")
                .parser(new Parser<ImageCaptureOptions>() {
                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(ImageCaptureOptions config, int i) {
                        return "ImageCaptureOptions with: " + config;
                    }

                    @Override
                    public String toVariableNameString(ImageCaptureOptions config) {
                        return this.toString(config, 0);
                    }
                }));
    }

}
