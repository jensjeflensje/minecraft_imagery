package dev.jensderuiter.minecraft_imagery.storage;

import com.github.davidmoten.aws.lw.client.BaseUrlFactory;

import java.util.Optional;

/**
 * A custom url factory class to use the S3 library with custom endpoints.
 */
public class CustomUrlFactory implements BaseUrlFactory {

    String endpoint;

    public CustomUrlFactory(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String create(String serviceName, Optional<String> region) {
        return this.endpoint;
    }

}
