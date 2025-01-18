package br.com.fechaki.telephone.v1.data.message;

import java.io.Serializable;

public record TelephoneMessageRequest(
        String telephoneId,
        String phoneNumber
) implements Serializable {
}
