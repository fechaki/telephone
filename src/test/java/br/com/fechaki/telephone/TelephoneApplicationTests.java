package br.com.fechaki.telephone;

import br.com.fechaki.telephone.controller.TelephoneV1Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.aot.DisabledInAotMode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisabledInAotMode
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = {TelephoneV1Controller.class, TestcontainersConfiguration.class})
class TelephoneApplicationTests {
    @Autowired
    private TelephoneV1Controller controllerV1;

    @Test
    @DisplayName("Validate controllers creation at context load")
    void contextLoads() {
        assertNotNull(controllerV1);
    }

}
