package br.com.fechaki.telephone.controller;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import br.com.fechaki.telephone.v1.mapper.TelephoneMapper;
import br.com.fechaki.telephone.v1.service.TelephoneService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/telephone")
public class TelephoneV1Controller {
    private final TelephoneMapper mapper;
    private final TelephoneService service;

    public TelephoneV1Controller(TelephoneService service) {
        this.service = service;
        this.mapper = Mappers.getMapper(TelephoneMapper.class);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TelephoneEntity> createAction(@RequestBody TelephoneRequest request) {
        TelephoneEntity input = mapper.toEntity(request);
        TelephoneEntity output = service.create(input);
        TelephoneResponse response = mapper.toResponse(output);

        URI url = URI.create(String.format("/api/v1/telephone/%s", response.telephoneId()));

        return ResponseEntity.created(url).build();
    }

    @GetMapping(value = "/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TelephoneResponse> readById(@PathVariable String id) {
        TelephoneEntity output = service.read(id);
        TelephoneResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{countryCode}/{areaCode}/{phoneNumber}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TelephoneResponse> readById(@PathVariable String countryCode, @PathVariable String areaCode, @PathVariable String phoneNumber) {
        TelephoneEntity output = service.read(countryCode, areaCode, phoneNumber);
        TelephoneResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}", consumes = ALL_VALUE, produces = ALL_VALUE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.delete(id);

        return ResponseEntity.accepted().build();
    }
}
