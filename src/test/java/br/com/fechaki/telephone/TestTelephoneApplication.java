package br.com.fechaki.telephone;

import org.springframework.boot.SpringApplication;

public class TestTelephoneApplication {

    public static void main(String[] args) {
        SpringApplication
            .from(TelephoneApplication::main)
            .with(TestcontainersConfiguration.class)
            .run(args);
    }

}
