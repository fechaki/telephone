package br.com.fechaki.telephone.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {
    private final BuildProperties buildProperties;

    private Contact getContact() {
        return new Contact()
                .name("Support API")
                .email("support.api@fechaki.com.br")
                .url("https://support.fechaki.com.br");
    }

    private List<Server> getServers() {
        Server local = new Server()
                .url("http://localhost:3700")
                .description("Local Environment");
        Server uat = new Server()
                .url("https://uat.api.fechaki.com.br")
                .description("UAT Environment");
        return List.of(local, uat);
    }

    private ExternalDocumentation getExternalDocumentation() {
        return new ExternalDocumentation()
                .description("Fechaki Telephone Wiki Documentation")
                .url("https://wiki.fechaki.com.br/docs");
    }

    private Info getInfo() {
        return new Info()
                .title("Fechaki Telephone API")
                .description("Telephone Module API Definitions")
                .summary("Telephone API for Fechaki App")
                .version(buildProperties.getVersion())
                .termsOfService("https://fechaki.com.br/terms")
                .license(new License().name("Apache 2.0").url("https://springdoc.org"));
    }

    @Bean
    public GroupedOpenApi telephoneV1OpenApi() {
        String[] paths = {"/api/v1/telephone/**"};
        String[] packagesToscan = {"br.com.fechaki.telephone.v1.controller"};

        return GroupedOpenApi.builder().group("Telephone - V1")
                .pathsToMatch(paths)
                .packagesToScan(packagesToscan)
                .build();
    }

    @Bean
    public OpenAPI fechakiTelephoneOpenAPI() {
        Info info = getInfo();
        info.contact(getContact());
        return new OpenAPI()
                .info(info)
                .servers(getServers())
                .externalDocs(getExternalDocumentation());
    }
}
