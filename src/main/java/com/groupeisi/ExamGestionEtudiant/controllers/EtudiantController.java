package com.groupeisi.ExamGestionEtudiant.controllers;

import com.groupeisi.ExamGestionEtudiant.dto.EtudiantDto;
import com.groupeisi.ExamGestionEtudiant.services.EtudiantService;
import com.groupeisi.ExamGestionEtudiant.services.FiliereService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/etudiants")
public class EtudiantController {



    private final EtudiantService etudiantService;
    private final FiliereService filiereService; // ✅ injection ici


    public EtudiantController(EtudiantService etudiantService , FiliereService filiereService) {
        this.etudiantService = etudiantService;
        this.filiereService = filiereService;

    }

    // Liste des étudiants
    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.getAll());
        return "etudiants/list";
    }

    // Formulaire d'ajout
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("etudiant", new EtudiantDto());
        model.addAttribute("filieres", filiereService.getAll()); // ✅ ajoute cette ligne

        return "etudiants/add";
    }

    // Sauvegarde de l'étudiant
    @PostMapping("/save")
    public String saveEtudiant(@ModelAttribute("etudiant") EtudiantDto dto) {
        etudiantService.create(dto);
        return "redirect:/etudiants";
    }
    // Formulaire d’édition
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EtudiantDto etudiant = etudiantService.getById(id);
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("filieres", filiereService.getAll());
        return "etudiants/edit";
    }


    // Soumission de l’édition
    @PostMapping("/update/{id}")
    public String updateEtudiant(@PathVariable Long id, @ModelAttribute("etudiant") EtudiantDto dto) {
        etudiantService.update(id, dto);
        return "redirect:/etudiants";
    }

    // Détail d’un étudiant
    @GetMapping("/details/{id}")
    public String getEtudiantDetails(@PathVariable Long id, Model model) {
        EtudiantDto etudiant = etudiantService.getById(id);
        model.addAttribute("etudiant", etudiant);
        return "etudiants/detail";
    }
    @PostMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.delete(id);
        return "redirect:/etudiants";
    }

}
