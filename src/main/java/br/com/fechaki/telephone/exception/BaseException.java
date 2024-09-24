package br.com.fechaki.telephone.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private ErrorMessageType errorMessageType;
    private HttpStatus httpStatus;

    public BaseException(String message) {
        super(message);
    }
}
