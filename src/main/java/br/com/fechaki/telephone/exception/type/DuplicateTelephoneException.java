package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class DuplicateTelephoneException extends BaseException {

    public DuplicateTelephoneException() {
        super("Telefone Informado já Cadastrado");
    }

    @Override
    public ErrorMessageType getErrorMessageType() {
        return ErrorMessageType.ERROR;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
