package br.com.fechaki.telephone.v1.mapper;

import br.com.fechaki.telephone.client.data.response.ClientEnrichmentResponse;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEnrichmentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneEnrichmentMapperTest {
    final TelephoneEnrichmentMapper mapper = Mappers.getMapper(TelephoneEnrichmentMapper.class);

    @Test
    @DisplayName("Telephone Validation Mapper - Happy Path")
    void toEntity() {
        ClientEnrichmentResponse response = new ClientEnrichmentResponse(true, "21999999999", "0, 0xx2199999-9999", "+5521999999999", "+55", "BR", "Brazil", "Rio de Janeiro", "Vivo", "Mobile");
        TelephoneEnrichmentEntity entity = mapper.toEntity(response);
        assertNotNull(entity);
        assertTrue(entity.isValid());
        assertEquals(response.number(), entity.getPhoneNumber());
        assertEquals(response.local_format(), entity.getLocalFormat());
        assertEquals(response.international_format(), entity.getInternationalFormat());
        assertEquals(response.country_prefix(), entity.getCountryPrefix());
        assertEquals(response.country_code(), entity.getCountryCode());
        assertEquals(response.country_name(), entity.getCountryName());
        assertEquals(response.location(), entity.getLocation());
        assertEquals(response.carrier(), entity.getCarrier());
        assertEquals(response.line_type(), entity.getLineType());
        assertNotNull(entity.getCreated());
    }

    @Test
    @DisplayName("Telephone Validation Mapper - Null")
    void toEntityNull() {
        TelephoneEnrichmentEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }
}