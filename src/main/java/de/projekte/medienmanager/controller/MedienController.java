package de.projekte.medienmanager.controller;

import de.projekte.medienmanager.model.MedienDatei;
import de.projekte.medienmanager.service.MedienService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/medien")
public class MedienController {
    private final MedienService medienService;

    public MedienController(MedienService medienService) {
        this.medienService = medienService;
    }

    @GetMapping
    public String listMedien(Model model) {
        List<MedienDatei> medien = medienService.getAllMedia();
        model.addAttribute("medien", medien);
        return "index";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadMedien(@RequestParam("file") MultipartFile file) throws IOException {
        medienService.saveFile(file);
        return "redirect:/medien";
    }

    @GetMapping("/delete/{id}")
    public String deleteMedien(@PathVariable Long id) {
        medienService.deleteMedia(id);
        return "redirect:/medien";
    }
}
