package feriados_nacionais.cv.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalculoPascoaService {

    /**
     * Calcula a data da Páscoa para um dado ano usando o algoritmo de Gauss
     * @param ano Ano para calcular a Páscoa
     * @return Data da Páscoa
     */
    public LocalDate calcularPascoa(int ano) {
        // Algoritmo de Gauss para cálculo da Páscoa
        int a = ano % 19;
        int b = ano / 100;
        int c = ano % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int n = (h + l - 7 * m + 114) / 31;
        int p = (h + l - 7 * m + 114) % 31;

        return LocalDate.of(ano, n, p + 1);
    }

    /**
     * Calcula a data de um feriado móvel baseado na Páscoa
     * @param ano Ano para calcular
     * @param diasDaPascoa Número de dias a adicionar/subtrair da Páscoa
     * @return Data do feriado móvel
     */
    public LocalDate calcularFeriadoMovel(int ano, int diasDaPascoa) {
        LocalDate pascoa = calcularPascoa(ano);
        return pascoa.plusDays(diasDaPascoa);
    }
}