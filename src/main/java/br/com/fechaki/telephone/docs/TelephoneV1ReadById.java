package br.com.fechaki.telephone.docs;

import br.com.fechaki.telephone.exception.type.TelephoneNotFoundException;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Operation(
    operationId = "readTelephoneById",
    description = "Read Telephone by ID",
    summary = "Operation for Read Telephone by Id",
    tags = "Telephone",
    parameters = {
        @Parameter(
            name = "id",
            description = "System telephone ID",
            required = true,
            example = "1db72170-bc07-49dd-a838-3840e2f82ebe"
        )
    },
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = TelephoneResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Telephone Not Found",
            content = @Content(schema = @Schema(implementation = TelephoneNotFoundException.class))
        )
    }
)
public @interface TelephoneV1ReadById {
}
