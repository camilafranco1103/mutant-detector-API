package MutantDetectorAPI.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnaRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name="DNA_sequence")
    private String dnaSequence;

    private boolean isMutant;

    // Constructor manual para usar en el controlador si es necesario
    public DnaRecord(String dnaSequence, boolean isMutant) {
        this.dnaSequence = dnaSequence;
        this.isMutant = isMutant;
    }
}
