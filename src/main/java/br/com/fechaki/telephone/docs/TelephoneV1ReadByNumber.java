package br.com.fechaki.telephone.docs;

import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
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
    operationId = "readTelephoneByNumber",
    description = "Read Telephone by Number",
    summary = "Operation for Read Telephone by Number",
    tags = "Telephone",
    parameters = {
        @Parameter(
            name = "countryCode",
            description = "Telephone Country Code",
            required = true,
            example = "55"
        ),
        @Parameter(
            name = "areaCode",
            description = "Telephone Area Code",
            required = true,
            example = "21"
        ),
        @Parameter(
            name = "phoneNumber",
            description = "Telephone Number",
            required = true,
            example = "966666666"
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
            content = @Content(examples = {@ExampleObject(value = "{\n\t\"type\": \"string\",\n\t\"title\": \"string\",\n\t\"status\": 0,\n\t\"detail\": \"string\",\n\t\"instance\": \"string\",\n\t\"errorMessageType\": \"string\",\n\t\"timestamp\": \"string\"\n}")})
        )
    }
)
public @interface TelephoneV1ReadByNumber {
}
