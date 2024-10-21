package MutantDetectorAPI.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MutantDetectorTest {

    @Autowired
    private MutantDetector mutantDetector;

    @Test
    void testIsMutant_Mutant() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testIsMutant_NotMutant() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGACGG", "GCGTCA", "TCACTG"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testIsMutant_InvalidDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGACGG", "GCGTCA", "TCACTZ"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("La secuencia de ADN contiene caracteres no válidos.", exception.getMessage());
    }

    @Test
    void testIsMutant_NonSquareMatrix() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGACGG", "GCGTCA"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("La matriz de ADN debe ser cuadrada (NxN).", exception.getMessage());
    }
}
