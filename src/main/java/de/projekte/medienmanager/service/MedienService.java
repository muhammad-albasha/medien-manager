package de.projekte.medienmanager.service;

import de.projekte.medienmanager.model.MedienDatei;
import de.projekte.medienmanager.repository.MedienRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MedienService {
    private final MedienRepository medienRepository;
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    public MedienService(MedienRepository medienRepository) {
        this.medienRepository = medienRepository;

        // Stelle sicher, dass der Upload-Ordner existiert
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    public MedienDatei saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Datei speichern
        file.transferTo(filePath.toFile());

        // MIME-Type ermitteln
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "unknown";
        }

        // In der Datenbank speichern
        MedienDatei media = new MedienDatei();
        media.setName(fileName);
        media.setUrl("/uploads/" + fileName);
        media.setMimeType(mimeType);
        return medienRepository.save(media);
    }

    public List<MedienDatei> getAllMedia() {
        return medienRepository.findAll();
    }

    public void deleteMedia(Long id) {
        Optional<MedienDatei> mediaOptional = medienRepository.findById(id);

        if (mediaOptional.isPresent()) {
            MedienDatei media = mediaOptional.get();

            // Datei-Pfad ermitteln
            Path filePath = Paths.get(UPLOAD_DIR, media.getName());
            File file = filePath.toFile();

            // Datei löschen
            if (file.exists() && file.delete()) {
                System.out.println("✅ Datei gelöscht: " + file.getAbsolutePath());
            } else {
                System.out.println("⚠ Datei nicht gefunden oder konnte nicht gelöscht werden!");
            }

            // Datenbank-Eintrag löschen
            medienRepository.deleteById(id);
        }
    }
}
