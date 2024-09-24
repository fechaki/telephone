package br.com.fechaki.telephone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TelephoneApplicationTests {

    @Test
    void contextLoads() {
    }

}
