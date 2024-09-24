package br.com.fechaki.telephone.v1.mapper;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneMapperTest {
    final TelephoneMapper mapper = Mappers.getMapper(TelephoneMapper.class);

    @Test
    @DisplayName("Mapper Request to Entity")
    void toEntity() {
        TelephoneRequest request = new TelephoneRequest("55", "21", "999999999");
        TelephoneEntity entity = mapper.toEntity(request);
        assertNotNull(entity);
        assertEquals("55", entity.getCountryCode());
        assertEquals("21", entity.getAreaCode());
        assertEquals("999999999", entity.getPhoneNumber());
    }

    @Test
    @DisplayName("Mapper Null Request to Entity")
    void toEntityNull() {
        TelephoneEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    @DisplayName("Mapper Entity to Response")
    void toResponse() {
        UUID uuid = UUID.randomUUID();
        TelephoneEntity entity = new TelephoneEntity(uuid, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneResponse response = mapper.toResponse(entity);
        assertNotNull(response);
        assertEquals(uuid, entity.getTelephoneId());
        assertEquals("55", response.countryCode());
        assertEquals("21", response.areaCode());
        assertEquals("988888888", response.phoneNumber());
    }

    @Test
    @DisplayName("Mapper Entity to Response with null Id")
    void toResponseNullId() {
        TelephoneEntity entity = new TelephoneEntity(null, "55", "21", "988888888", Boolean.TRUE, null, false, LocalDateTime.now(), LocalDateTime.now());
        TelephoneResponse response = mapper.toResponse(entity);
        assertNotNull(response);
        assertNull(entity.getTelephoneId());
        assertEquals("55", response.countryCode());
        assertEquals("21", response.areaCode());
        assertEquals("988888888", response.phoneNumber());
    }

    @Test
    @DisplayName("Mapper Null Entity to Response")
    void toResponseNull() {
        TelephoneResponse response = mapper.toResponse(null);
        assertNull(response);
    }
}