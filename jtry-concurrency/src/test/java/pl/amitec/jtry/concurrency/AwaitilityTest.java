package pl.amitec.jtry.concurrency;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AwaitilityTest {
    @Test
    public void testAtomic() {
        AtomicLong counter = new AtomicLong();
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter.incrementAndGet();
        });

        with().atMost(200, MILLISECONDS).await().untilAtomic(counter, is(1L));
    }
    @Test
    public void testDelayedAtomic() {
        AtomicLong counter = new AtomicLong();
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter.incrementAndGet();
        });

        ConditionTimeoutException thrown = Assertions.assertThrows(ConditionTimeoutException.class, () -> {
            with().atLeast(200, MILLISECONDS).await().untilAtomic(counter, is(1L));
        });

        assertThat(thrown, is(notNullValue()));
    }
}
