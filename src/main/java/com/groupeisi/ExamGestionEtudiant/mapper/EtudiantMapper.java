package com.groupeisi.ExamGestionEtudiant.mapper;


import com.groupeisi.ExamGestionEtudiant.dto.EtudiantDto;
import com.groupeisi.ExamGestionEtudiant.entities.EtudiantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EtudiantMapper {

    @Mapping(source = "filiere.id", target = "filiereId")
    EtudiantDto toDto(EtudiantEntity entity);

    @Mapping(target = "filiere", ignore = true)
    EtudiantEntity toEntity(EtudiantDto dto);

    default EtudiantEntity toEntity(EtudiantDto dto, com.groupeisi.ExamGestionEtudiant.entities.FiliereEntity filiere) {
        EtudiantEntity entity = toEntity(dto);
        entity.setFiliere(filiere);
        return entity;
    }
}


