package MutantDetectorAPI.Controller;

import MutantDetectorAPI.Entity.DnaRecord;
import MutantDetectorAPI.Repository.DnaRecordRepository;
import MutantDetectorAPI.Validator.MutantDetector;
import MutantDetectorAPI.dto.DnaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MutantControllerTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantController mutantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsMutant_Mutant() {
        DnaRequest dnaRequest = new DnaRequest(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        when(mutantDetector.isMutant(any())).thenReturn(true);
        when(dnaRecordRepository.findByDnaSequence(anyString())).thenReturn(null);

        ResponseEntity<String> response = mutantController.isMutant(dnaRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mutant detected!", response.getBody());
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void testIsMutant_NotMutant() {
        DnaRequest dnaRequest = new DnaRequest(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGACGG", "GCGTCA", "TCACTG"});
        when(mutantDetector.isMutant(any())).thenReturn(false);
        when(dnaRecordRepository.findByDnaSequence(anyString())).thenReturn(null);

        ResponseEntity<String> response = mutantController.isMutant(dnaRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not a mutant", response.getBody());
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void testIsMutant_InvalidDna() {
        DnaRequest dnaRequest = new DnaRequest(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGACGG", "GCGTCA", "TCACTZ"});
        when(mutantDetector.isMutant(any())).thenThrow(new IllegalArgumentException("La secuencia de ADN contiene caracteres no válidos."));

        ResponseEntity<String> response = mutantController.isMutant(dnaRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La secuencia de ADN contiene caracteres no válidos.", response.getBody());
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }
}
