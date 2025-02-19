package de.projekte.medienmanager.repository;


import de.projekte.medienmanager.model.MedienDatei;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedienRepository extends JpaRepository<MedienDatei, Long> {
}
