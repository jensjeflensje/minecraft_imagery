package dev.jensderuiter.minecraft_imagery.storage;

public class StorageException extends Exception {

    /**
     * Initialize an error in the storage system.
     * @param message The message to be shown.
     */
    public StorageException(String message) {
        super(message);
    }

}
