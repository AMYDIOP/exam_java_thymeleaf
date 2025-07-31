package com.groupeisi.ExamGestionEtudiant.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EtudiantDto {
    private Long id;

    @NotBlank(message = "Le nom ne doit pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne doit pas être vide")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format de l'email invalide")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateNaissance;

    @NotBlank(message = "La classe ne doit pas être vide")
    private String classe;

    @NotNull(message = "La filière est obligatoire")
    private Long filiereId;
}

