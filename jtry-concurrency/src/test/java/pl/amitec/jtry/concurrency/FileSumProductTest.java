package pl.amitec.jtry.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSumProductTest {

    @Test
    public void getResult() {
        FileSumProduct fileSumProduct = new FileSumProduct();
        var result = fileSumProduct.getResult(
                "src/test/resources/numbers1.txt",
                "src/test/resources/numbers2.txt");
        System.out.println("Result: " + result);
        assertThat(result, is(90L));
    }

    @Test
    public void getFailed() {
        FileSumProduct fileSumProduct = new FileSumProduct();
        assertThrows(CompletionException.class, () -> {
            fileSumProduct.getResult(
                    "src/test/resources/numbers1.txt",
                    "src/test/resources/numbers2.txt",
                    "src/test/resources/numbers-non-existent.txt");
        });
    }

}
