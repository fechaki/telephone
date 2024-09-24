package br.com.fechaki.telephone.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
public class BaseException extends ErrorResponseException {

    public BaseException(String title, String message, ErrorMessageType errorMessageType, HttpStatus httpStatus) {
        super(httpStatus, asProblemDetail(title, message, errorMessageType, httpStatus), null);
    }

    private static ProblemDetail asProblemDetail(String title, String message, ErrorMessageType errorMessageType, HttpStatus httpStatus) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus.value());
        problemDetail.setDetail(message);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("https://api.fechaki.com.br/errors/" + httpStatus.name().toLowerCase() + "/" + errorMessageType.toString().toLowerCase()));
        problemDetail.setProperty("errorMessageType", errorMessageType);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
