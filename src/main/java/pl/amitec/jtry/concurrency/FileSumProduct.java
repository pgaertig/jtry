package pl.amitec.jtry.concurrency;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class FileSumProduct {
    public static class SumFile implements Callable<Long> {
        private Path filePath;

        public SumFile(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public Long call() throws Exception {
            return Files.lines(filePath)
                    .map(Long::parseLong)
                    .reduce(0L, Long::sum);
        }
    }

    public Long getResult(String... filePaths) {
        List<CompletableFuture<Long>> futures = Arrays.stream(filePaths)
                .map(Paths::get)
                .map(path -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return new SumFile(path).call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Optional<Long> result = futures.stream()
                .map(CompletableFuture::join)
                .reduce((a, b) -> a * b);

        return result.orElse(0L);
    }

}
