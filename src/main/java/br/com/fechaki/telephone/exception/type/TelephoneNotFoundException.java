package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class TelephoneNotFoundException extends BaseException {
    public TelephoneNotFoundException(String id) {
        super(
            "Nenhum Telefone encontrado",
            "O Telefone " + id + " não foi encontrado",
            ErrorMessageType.WARNING,
            HttpStatus.NOT_FOUND
        );
    }
}
