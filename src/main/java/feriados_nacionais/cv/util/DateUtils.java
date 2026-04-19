package feriados_nacionais.cv.util;

import java.time.DayOfWeek;
import java.util.Map;

public class DateUtils {
    
    private static final Map<DayOfWeek, String> DIAS_SEMANA_PT = Map.of(
        DayOfWeek.MONDAY, "Segunda-feira",
        DayOfWeek.TUESDAY, "Terça-feira", 
        DayOfWeek.WEDNESDAY, "Quarta-feira",
        DayOfWeek.THURSDAY, "Quinta-feira",
        DayOfWeek.FRIDAY, "Sexta-feira",
        DayOfWeek.SATURDAY, "Sábado",
        DayOfWeek.SUNDAY, "Domingo"
    );
    
    public static String getDiaSemanaPortugues(DayOfWeek dayOfWeek) {
        return DIAS_SEMANA_PT.get(dayOfWeek);
    }
}