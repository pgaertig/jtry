package pl.amitec.jtry.concurrency;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeoutException;

/**
 * Structured concurrency implementation.
 * Stops all subtasks on first failure.
 * Also stops all subtasks after timeout.
 */
public class FileSumProductStructuredConcurrency {


    /**
     * Returns product of sum of numbers from files.
     * In case of any exception results in FileSumException.
     *
     * @param filePaths paths to files with numbers in lines
     * @return long value
     */
    public Long getResult(String... filePaths) throws InterruptedException {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<Subtask<Long>> fileResults = Arrays.stream(filePaths)
                    .map(Paths::get)
                    .map(path -> scope.fork(() -> {
                            try(var lines = Files.lines(path)) {
                                return lines.map(Long::parseLong).reduce(0L, Long::sum);
                            }
                        }))
                    .toList();

            //example of timeout
            scope.joinUntil(Instant.now().plusSeconds(5));
            scope.throwIfFailed(e -> {
                throw new FileSumException(e.getMessage(), e);
            });

            return fileResults.stream().map(Subtask::get).reduce((a, b) -> a * b).orElse(0L);

        } catch (TimeoutException e) {
            throw new FileSumException("Calculation took too long", e);
        }
    }
}
