package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class TelephoneNotExistException extends BaseException {
    public TelephoneNotExistException(String id) {
        super(
            "Telefone Inexistente",
            "ID do Telefone informado não existe (" + id + ")",
            ErrorMessageType.ERROR,
            HttpStatus.NOT_FOUND
        );
    }
}
