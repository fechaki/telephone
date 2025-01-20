package br.com.fechaki.telephone.docs;

import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Operation(
    operationId = "createTelephone",
    description = "Create New Telephone",
    summary = "Operation for create a new Telephone",
    tags = "Telephone",
    requestBody = @RequestBody(
        required = true,
        description = "Telephone Country Code, Area Code and Number",
        content = @Content(schema = @Schema(implementation = TelephoneRequest.class))
    ),
    responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            headers = @Header(name = "Location", description = "Path to get the created Telephone"),
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Telephone already registered",
            content = @Content(examples = {@ExampleObject(value = "{\n\t\"type\": \"string\",\n\t\"title\": \"string\",\n\t\"status\": 0,\n\t\"detail\": \"string\",\n\t\"instance\": \"string\",\n\t\"errorMessageType\": \"string\",\n\t\"timestamp\": \"string\"\n}")})
       )
    }
)
public @interface TelephoneV1Create {
}
