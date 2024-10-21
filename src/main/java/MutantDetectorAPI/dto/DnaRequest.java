package MutantDetectorAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnaRequest {
    // Campo que contendrá la secuencia de ADN.
    private String[] dna;
}
