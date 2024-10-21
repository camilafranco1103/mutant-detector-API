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

        // Check horizontal sequences
        for (String row : dna) {
            if (checkSequence(row)) {
                count++;
            }
        }

        // Check vertical sequences
        for (int col = 0; col < n; col++) {
            StringBuilder vertical = new StringBuilder();
            for (int row = 0; row < n; row++) {
                vertical.append(dna[row].charAt(col));
            }
            if (checkSequence(vertical.toString())) {
                count++;
            }
        }

        // Check diagonal sequences
        for (int i = 0; i < n - 3; i++) {
            for (int j = 0; j < n - 3; j++) {
                if (checkDiagonal(dna, i, j, 1, 1) || checkDiagonal(dna, i, j, 1, -1)) {
                    count++;
                }
            }
        }

        return count > 1;
    }

    private boolean checkSequence(String seq) {
        for (int i = 0; i < seq.length() - 3; i++) {
            if (seq.charAt(i) == seq.charAt(i + 1) && seq.charAt(i) == seq.charAt(i + 2) && seq.charAt(i) == seq.charAt(i + 3)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(String[] dna, int startRow, int startCol, int rowIncrement, int colIncrement) {
        int n = dna.length;
        for (int i = 0; i < n - 3; i++) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;
            if (row >= n || col >= n || col < 0) {
                break;
            }
            if (row + 3 * rowIncrement >= n || col + 3 * colIncrement >= n || col + 3 * colIncrement < 0) {
                break;
            }
            if (dna[row].charAt(col) == dna[row + rowIncrement].charAt(col + colIncrement) &&
                    dna[row].charAt(col) == dna[row + 2 * rowIncrement].charAt(col + 2 * colIncrement) &&
                    dna[row].charAt(col) == dna[row + 3 * rowIncrement].charAt(col + 3 * colIncrement)) {
                return true;
            }
        }
        return false;
    }
}
