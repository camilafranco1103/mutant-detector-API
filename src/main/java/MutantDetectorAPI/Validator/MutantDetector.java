package MutantDetectorAPI.Validator;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {

    public boolean isMutant(String[] dna) {
        // Validar que la matriz de ADN sea cuadrada (NxN)
        if (!isSquareMatrix(dna)) {
            throw new IllegalArgumentException("La matriz de ADN debe ser cuadrada (NxN).");
        }

        // Validar que cada letra en la secuencia de ADN sea una de las letras válidas (A, T, C, G)
        if (!isValidDnaSequence(dna)) {
            throw new IllegalArgumentException("La secuencia de ADN contiene caracteres no válidos.");
        }

        return checkMutant(dna);
    }

    private boolean isSquareMatrix(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }
        int n = dna.length;
        for (String row : dna) {
            if (row.length() != n) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDnaSequence(String[] dna) {
        for (String row : dna) {
            for (char c : row.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkMutant(String[] dna) {
        int n = dna.length;
        int count = 0;

        // Check horizontal, vertical, and diagonal sequences
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j + 3 < n) {
                    if (checkSequence(dna[i].substring(j, j + 4))) {
                        count++;
                    }
                }
                if (i + 3 < n) {
                    if (checkSequence(dna[i].charAt(j) + "" + dna[i + 1].charAt(j) + "" + dna[i + 2].charAt(j) + "" + dna[i + 3].charAt(j))) {
                        count++;
                    }
                    if (j + 3 < n) {
                        if (checkSequence(dna[i].charAt(j) + "" + dna[i + 1].charAt(j + 1) + "" + dna[i + 2].charAt(j + 2) + "" + dna[i + 3].charAt(j + 3))) {
                            count++;
                        }
                    }
                    if (j - 3 >= 0) {
                        if (checkSequence(dna[i].charAt(j) + "" + dna[i + 1].charAt(j - 1) + "" + dna[i + 2].charAt(j - 2) + "" + dna[i + 3].charAt(j - 3))) {
                            count++;
                        }
                    }
                }
            }
        }

        return count > 1;
    }

    private boolean checkSequence(String seq) {
        return seq.charAt(0) == seq.charAt(1) && seq.charAt(1) == seq.charAt(2) && seq.charAt(2) == seq.charAt(3);
    }
}
