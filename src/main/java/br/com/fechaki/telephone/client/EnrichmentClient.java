package br.com.fechaki.telephone.client;

import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/validate")
public interface EnrichmentClient {
    @GetExchange
    ClientEnrichmentResponse validate(
            @RequestParam("access_key") String accessKey,
            @RequestParam("number") String number,
            @RequestParam("country_code") String countryCode,
            @RequestParam("format") String format
    );
}