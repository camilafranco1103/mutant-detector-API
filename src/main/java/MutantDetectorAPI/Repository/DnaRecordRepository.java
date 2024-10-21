package MutantDetectorAPI.Repository;

import MutantDetectorAPI.Entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    // Método para buscar un ADN por su secuencia
    DnaRecord findByDnaSequence(String dnaSequence);

    // Métodos para contar registros de ADN mutante y humano
    long countByIsMutant(boolean isMutant);
}
