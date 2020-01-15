package nl.com.kalah.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for the application.
 *
 * @author Diego
 * @see KalahApplication
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
public class KalahApplicationTest {

    private static final String[] ARGS = {};

    @Test
    public void contextLoadsTest() {
        KalahApplication.main(ARGS);
    }

}

