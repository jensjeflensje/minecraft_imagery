package dev.jensderuiter.minecraft_imagery.storage;

public class StorageRuntimeException extends RuntimeException {

    /**
     * Initialize an unexpected error in the storage system.
     * @param message The message to be shown.
     */
    public StorageRuntimeException(String message) {
        super(message);
    }

}
