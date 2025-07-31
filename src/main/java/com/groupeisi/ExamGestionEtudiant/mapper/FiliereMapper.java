package com.groupeisi.ExamGestionEtudiant.mapper;

import com.groupeisi.ExamGestionEtudiant.dto.FiliereDto;
import com.groupeisi.ExamGestionEtudiant.entities.FiliereEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FiliereMapper {
    FiliereDto toDto(FiliereEntity entity);
    FiliereEntity toEntity(FiliereDto dto);
}
