package br.com.fechaki.telephone.client.data.request;

public record ClientValidationRequest(
        String number,
        String countryCode,
        String format
) {
}
