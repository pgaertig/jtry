package pl.amitec.jtry.concurrency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

/**
 * Completable future implementation.
 */
public class FileSumProduct {

    /**
     * Returns product of sum of numbers from files.
     * In case of any exception results in CompletionException.
     * @param filePaths
     * @return long value
     */
    public Long getResult(String... filePaths) {
        List<CompletableFuture<Long>> futures = Arrays.stream(filePaths)
                .map(Paths::get)
                .map(path -> CompletableFuture.supplyAsync(() -> {
                    try(var lines = Files.lines(path)) {
                        return lines.map(Long::parseLong).reduce(0L, Long::sum);
                    } catch (IOException e) {
                        // this will be wrapped in CompletionException anyway
                        throw new FileSumException(e.getMessage(), e);
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
