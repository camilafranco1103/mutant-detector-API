package MutantDetectorAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
    private long countMutantDna;
    private long countHumanDna;
    private double ratio;
}
