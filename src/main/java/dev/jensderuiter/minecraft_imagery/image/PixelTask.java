package dev.jensderuiter.minecraft_imagery.image;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

class PixelTask<V> implements Function<ExecutorService, CompletableFuture<V>> {

    public class PixelTaskParameters {
        public double pitch;
        public double yaw;
        public int x;
        public int y;
    };

    private Function<PixelTaskParameters, V> task;
    private PixelTaskParameters params;

    public PixelTask(Function<PixelTaskParameters, CompletableFuture<V>> task, PixelTaskParameters params) {
        this.params = params;
    }

    public CompletableFuture<V> executeWith(ExecutorService pool) {
        CompletableFuture<V> theFuture = new CompletableFuture<>();
        pool.submit(() -> {
            V result = this.task.apply(params);
            theFuture.complete(result);
        });

        return theFuture;
    }

    @Override
    public CompletableFuture<V> apply(ExecutorService pool) {
        return this.executeWith(pool);
    }
}

