package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class TelephoneOTPException extends BaseException {
    public TelephoneOTPException(String telephoneId) {
        super("Validação OTP",
                "Falha na validação OTP Telefone Id (" + telephoneId + ")",
                ErrorMessageType.ERROR,
                HttpStatus.BAD_REQUEST);
    }
}
