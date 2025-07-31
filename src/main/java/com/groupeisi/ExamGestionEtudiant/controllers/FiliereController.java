package com.groupeisi.ExamGestionEtudiant.controllers;

import com.groupeisi.ExamGestionEtudiant.dto.FiliereDto;
import com.groupeisi.ExamGestionEtudiant.services.FiliereService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/filieres")
public class FiliereController {

    private final FiliereService filiereService;

    public FiliereController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    // Liste des filières
    @GetMapping
    public String listFilieres(Model model) {
        model.addAttribute("filieres", filiereService.getAll());
        return "filieres/list";
    }

    // Formulaire d'ajout
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("filiere", new FiliereDto());
        return "filieres/add";
    }

    // Sauvegarde d’une filière
    @PostMapping("/save")
    public String saveFiliere(@ModelAttribute("filiere") FiliereDto dto) {
        filiereService.create(dto);
        return "redirect:/filieres";
    }

    // Détail d’une filière
    @GetMapping("/details/{id}")
    public String getFiliereDetails(@PathVariable Long id, Model model) {
        FiliereDto filiere = filiereService.getById(id);
        model.addAttribute("filiere", filiere);
        return "filieres/detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("filiere", filiereService.getById(id));
        return "filieres/edit";
    }

    // Mise à jour de la filière
    @PostMapping("/update/{id}")
    public String updateFiliere(@PathVariable Long id, @ModelAttribute("filiere") FiliereDto dto) {
        filiereService.update(id, dto);
        return "redirect:/filieres";
    }

    // Suppression d’une filière
    @GetMapping("/delete/{id}")
    public String deleteFiliere(@PathVariable Long id) {
        filiereService.delete(id);
        return "redirect:/filieres";
    }
}
