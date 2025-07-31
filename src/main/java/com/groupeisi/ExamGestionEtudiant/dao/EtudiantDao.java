package com.groupeisi.ExamGestionEtudiant.dao;


import com.groupeisi.ExamGestionEtudiant.entities.EtudiantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantDao extends JpaRepository<EtudiantEntity, Long> {
}

