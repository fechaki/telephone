package br.com.fechaki.telephone.v1.mapper;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.message.TelephoneMessageRequest;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface TelephoneMapper {
    TelephoneEntity toEntity(TelephoneRequest param);
    TelephoneResponse toResponse(TelephoneEntity entity);
    @Mapping(source = "telephoneEntity", target = "phoneNumber", qualifiedByName = "buildNumberFormat")
    TelephoneMessageRequest toMessageRequest(TelephoneEntity telephoneEntity);

    @Named("buildNumberFormat")
    default String buildNumberFormat(TelephoneEntity entity) {
        return entity.getAreaCode() + entity.getPhoneNumber();
    }
}
