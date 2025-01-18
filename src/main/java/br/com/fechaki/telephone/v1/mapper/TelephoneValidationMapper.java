package br.com.fechaki.telephone.v1.mapper;

import br.com.fechaki.telephone.client.data.response.ClientValidationResponse;
import br.com.fechaki.telephone.v1.data.entity.TelephoneValidationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TelephoneValidationMapper {
    @Mapping(source = "number", target = "phoneNumber")
    @Mapping(source = "local_format", target = "localFormat")
    @Mapping(source = "international_format", target = "internationalFormat")
    @Mapping(source = "country_prefix", target = "countryPrefix")
    @Mapping(source = "country_code", target = "countryCode")
    @Mapping(source = "country_name", target = "countryName")
    @Mapping(source = "line_type", target = "lineType")
    TelephoneValidationEntity toEntity(ClientValidationResponse response);
}