package br.com.fechaki.telephone.client.config;

import br.com.fechaki.telephone.client.EnrichmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class EnrichmentClientConfig {
    @Value("${fechaki.telephone.enrichment.url}")
    private String uri;

    @Bean
    EnrichmentClient enrichmentClient() {
        RestClient client = RestClient.builder()
                .baseUrl(uri)
                .requestInterceptor((request, body, execution) -> { log.info("Executing request: {}", request.getURI()); return execution.execute(request, body); })
        .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
        .build();
        return factory.createClient(EnrichmentClient.class);
    }
}
