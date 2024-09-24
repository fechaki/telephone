package br.com.fechaki.telephone.v1.data.request;

public record TelephoneRequest(
    String countryCode,
    String areaCode,
    String phoneNumber
) {
}
