package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class DuplicateTelephoneException extends BaseException {

    public DuplicateTelephoneException(String countryCode, String areaCode, String phoneNumber) {
        super(
            "Telefone já Cadastrado",
            "Telefone Informado (" + countryCode + areaCode + phoneNumber + ") já se encontra em uso",
            ErrorMessageType.ERROR,
            HttpStatus.BAD_REQUEST
        );
    }
}
