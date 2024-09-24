package br.com.fechaki.telephone.exception.type;

import br.com.fechaki.telephone.exception.BaseException;
import br.com.fechaki.telephone.exception.ErrorMessageType;
import org.springframework.http.HttpStatus;

public class TelephoneNotExistException extends BaseException {
    public TelephoneNotExistException() {
        super("ID do Telefone informado não existe");
    }

    @Override
    public ErrorMessageType getErrorMessageType() {
        return ErrorMessageType.ERROR;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
