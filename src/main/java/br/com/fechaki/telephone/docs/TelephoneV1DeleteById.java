package br.com.fechaki.telephone.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Operation(
    operationId = "deleteTelephoneById",
    description = "Delete Telephone by ID",
    summary = "Operation for Delete Telephone by Id",
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
            responseCode = "202",
            description = "Accepted",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Telephone Not Exist",
            content = @Content(examples = {@ExampleObject(value = "{\n\t\"type\": \"string\",\n\t\"title\": \"string\",\n\t\"status\": 0,\n\t\"detail\": \"string\",\n\t\"instance\": \"string\",\n\t\"errorMessageType\": \"string\",\n\t\"timestamp\": \"string\"\n}")})
        )
    }
)
public @interface TelephoneV1DeleteById {
}
