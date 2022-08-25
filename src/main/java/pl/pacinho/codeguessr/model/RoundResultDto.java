package pl.pacinho.codeguessr.model;

import java.math.BigDecimal;

public record RoundResultDto(
        BigDecimal result,
        String[] partsOfCorrectPath,
        String[] partsOfSelectedPath,
        Integer correctLineNumber,
        Integer selectedLineNumber
) {
}
