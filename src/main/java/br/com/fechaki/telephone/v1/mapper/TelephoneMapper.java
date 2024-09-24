package br.com.fechaki.telephone.v1.mapper;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.request.TelephoneRequest;
import br.com.fechaki.telephone.v1.data.response.TelephoneResponse;
import org.mapstruct.Mapper;

@Mapper
public interface TelephoneMapper {
    TelephoneEntity toEntity(TelephoneRequest param);
    TelephoneResponse toResponse(TelephoneEntity entity);
}
