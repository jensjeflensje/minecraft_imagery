package dev.jensderuiter.minecraft_imagery.storage;

import com.github.davidmoten.aws.lw.client.Client;
import com.github.davidmoten.aws.lw.client.HttpMethod;
import org.bukkit.Bukkit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class S3StorageProvider implements StorageProvider {

    private Client s3;
    private String bucket;

    /**
     * Initialize the S3 storage provider.
     * @param endpoint S3 endpoint url.
     * @param region S3 region (auto for CloudFlare R2).
     * @param accessKey S3 access key.
     * @param secretKey S3 secret key.
     * @param bucket S3 bucket.
     */
    public S3StorageProvider(
            String endpoint,
            String region,
            String accessKey,
            String secretKey,
            String bucket
    ) {
        this.s3 = Client.s3()
                .region(region)
                .accessKey(accessKey)
                .secretKey(secretKey)
                .baseUrlFactory(new CustomUrlFactory(endpoint))
                .build();
        this.bucket = bucket;

        int fileCount = this.s3
                .path(this.bucket)
                .responseAsXml()
                .childrenWithName("Contents")
                .size();

        Bukkit.getLogger().info(String.format(
                "S3 storage successfully initialized (currently containing %d entries)", fileCount));
    }

    /**
     * Fetch an image from the S3 bucket. Image is in PNG format (TYPE_INT_ARGB).
     * @param uuid The UUID of the image to fetch. You have saved this from the store method.
     * @return The image with the specified UUID.
     * @throws StorageException When the fetching fails.
     */
    @Override
    public BufferedImage fetch(UUID uuid) throws StorageException {
        String fileName = getFileName(uuid.toString());
        byte[] bytes = s3
                .path(this.bucket, fileName)
                .responseAsBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new StorageException(
                    String.format(
                            "Fetching %s failed: %s",
                            fileName,
                            e.getMessage()
                    )
            );
        }
    }

    /**
     * Store an image in the S3 bucket. Will be stored as (generated uuid).png
     * @param image The image to store.
     * @return The UUID of the image you've just stored. Used to retrieve or remove the image.
     * @throws StorageException When storing the image fails.
     */
    @Override
    public UUID store(BufferedImage image) throws StorageException {
        while (true) {
            UUID uuid = UUID.randomUUID();
            String fileName = getFileName(uuid.toString());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, getExtension(), outputStream);
            } catch (IOException e) {
                throw new StorageException(
                        String.format(
                                "Writing %s failed: %s",
                                fileName,
                                e.getMessage()
                        )
                );
            }
            byte[] bytes = outputStream.toByteArray();

            s3
                .path(this.bucket, fileName)
                .method(HttpMethod.PUT)
                .requestBody(bytes)
                .execute();

            return uuid;
        }
    }

    /**
     * Remove an image from the S3 bucket.
     * @param uuid The UUID of the image to remove. You have saved this from the store method.
     * @throws StorageException When the deletion fails.
     */
    @Override
    public void remove(UUID uuid) throws StorageException {
        String fileName = getFileName(uuid.toString());
        try {
            s3.path(this.bucket, fileName)
                    .method(HttpMethod.DELETE)
                    .execute();
        } catch (RuntimeException e) {
            throw new StorageException(
                    String.format(
                            "Removing %s failed: %s",
                            fileName,
                            e.getMessage()
                    )
            );
        }
    }

    @Override
    public boolean downloadUrlEnabled() {
        return true;
    }

    /**
     * Generate an S3 presigned url that's valid for one day.
     * @param uuid The UUID of the image to fetch. You have saved this from the store method.
     * @return The image with the specified UUID.
     * @throws StorageException When the fetching fails.
     */
    @Override
    public String generateDownloadUrl(UUID uuid) throws StorageException {
        String fileName = getFileName(uuid.toString());
        try {
            return s3
                    .path(this.bucket, fileName)
                    .presignedUrl(1, TimeUnit.DAYS);
        } catch (RuntimeException e) {
            throw new StorageException(
                    String.format(
                            "Generating download url for %s failed: %s",
                            fileName,
                            e.getMessage()
                    )
            );
        }
    }
}
