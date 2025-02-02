package br.com.fechaki.telephone.client.data.response;

public record ClientEnrichmentResponse(
    boolean valid,
    String number,
    String local_format,
    String international_format,
    String country_prefix,
    String country_code,
    String country_name,
    String location,
    String carrier,
    String line_type
) {
}
