package br.com.fechaki.telephone.v1.data.message;

public record TelephoneStatusUpdaterMessageRequest(
        String telephoneId,
        Boolean activated,
        Boolean validated
) {
}
