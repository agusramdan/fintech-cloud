package ramdan.project.fintech.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApplicationTest {

    @LocalServerPort
    private int port;

    //@Before
    void setUp() {
        //RestAssured.port = port;
    }

    @Test
    void data() {

        assertTrue(true);

    }
}
