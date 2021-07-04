package app.ldev.estoqueCervejas.mapper;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CervejaMapper {

    CervejaMapper INSTANCE = Mappers.getMapper(CervejaMapper.class);

    Cerveja toModel(CervejaDTO cervejaDTO);

    CervejaDTO toDTO(Cerveja cerveja);
}
