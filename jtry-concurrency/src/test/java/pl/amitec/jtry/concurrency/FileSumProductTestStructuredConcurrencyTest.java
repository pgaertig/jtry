package pl.amitec.jtry.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSumProductTestStructuredConcurrencyTest {
    @Test
    public void getResult() throws InterruptedException, ExecutionException {
        FileSumProductStructuredConcurrency fileSumProduct = new FileSumProductStructuredConcurrency();
        var result = fileSumProduct.getResult(
                "src/test/resources/numbers1.txt",
                "src/test/resources/numbers2.txt");
        System.out.println("Result: " + result);
        assertThat(result, is(90L));
    }

    @Test
    public void getFailed() {
        FileSumProductStructuredConcurrency fileSumProduct = new FileSumProductStructuredConcurrency();
        assertThrows(FileSumException.class, () -> {
            fileSumProduct.getResult(
                    "src/test/resources/numbers1.txt",
                    "src/test/resources/numbers2.txt",
                    "src/test/resources/numbers-non-existent.txt");
        });
    }
}
