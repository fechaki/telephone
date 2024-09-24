package br.com.fechaki.telephone.v1.data.response;

public record TelephoneResponse(
    String telephoneId,
    String countryCode,
    String areaCode,
    String phoneNumber
) {
}
