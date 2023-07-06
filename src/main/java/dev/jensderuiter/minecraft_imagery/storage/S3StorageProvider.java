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

// TODO: javadocs
public class S3StorageProvider implements StorageProvider {

    private Client s3;
    private String bucket;

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

        // TODO: make this work
        // count is not correct and it probably has something to do with the response
        // (we're requesting the wrong thing)
        int fileCount = this.s3
                .path()
                .responseAsXml()
                .childrenWithName("Contents")
                .size();

        Bukkit.getLogger().info(String.format(
                "S3 storage successfully initialized (currently containing %d entries)", fileCount));
    }

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
}
