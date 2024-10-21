package MutantDetectorAPI.Controller;

import MutantDetectorAPI.Entity.DnaRecord;
import MutantDetectorAPI.Repository.DnaRecordRepository;
import MutantDetectorAPI.Validator.MutantDetector;
import MutantDetectorAPI.dto.DnaRequest;
import MutantDetectorAPI.dto.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    // Inyección de dependencias a través del constructor
    @Autowired
    public MutantController(MutantDetector mutantDetector, DnaRecordRepository dnaRecordRepository) {
        this.mutantDetector = mutantDetector;
        this.dnaRecordRepository = dnaRecordRepository;
    }

    @PostMapping("/")
    public ResponseEntity<String> isMutant(@RequestBody DnaRequest dnaRequest) {
        try {
            String dnaSequence = String.join(",", dnaRequest.getDna());
            DnaRecord existingRecord = dnaRecordRepository.findByDnaSequence(dnaSequence);

            if (existingRecord != null) {
                return existingRecord.isMutant() ?
                        ResponseEntity.ok("Mutant detected!") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a mutant");
            }

            boolean isMutant = mutantDetector.isMutant(dnaRequest.getDna());
            DnaRecord dnaRecord = new DnaRecord(dnaSequence, isMutant);
            dnaRecordRepository.save(dnaRecord);

            return isMutant ?
                    ResponseEntity.ok("Mutant detected!") :
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a mutant");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        long countMutantDna = dnaRecordRepository.countByIsMutant(true);
        long countHumanDna = dnaRecordRepository.countByIsMutant(false);
        double ratio = countHumanDna > 0 ? (double) countMutantDna / countHumanDna : 0;

        StatsResponse statsResponse = new StatsResponse(countMutantDna, countHumanDna, ratio);
        return ResponseEntity.ok(statsResponse);
    }
}
