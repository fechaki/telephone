package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class TelephoneEnrichmentException extends BaseException {
    public TelephoneEnrichmentException(String telephoneId) {
        super("Telefone já Validado",
                "Telefone Id (" + telephoneId + ") já se encontra validado",
                ErrorMessageType.WARNING,
                HttpStatus.CONFLICT);
    }
}
