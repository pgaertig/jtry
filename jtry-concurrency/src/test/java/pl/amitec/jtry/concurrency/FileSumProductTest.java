package pl.amitec.jtry.concurrency;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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
}
