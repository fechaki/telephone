package br.com.fechaki.telephone.client.data.request;

public record ClientEnrichmentRequest(
        String number,
        String countryCode,
        String format
) {
}
