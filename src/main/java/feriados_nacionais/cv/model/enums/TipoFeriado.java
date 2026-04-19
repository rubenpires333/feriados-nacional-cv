package feriados_nacionais.cv.model.enums;

public enum TipoFeriado {
    NACIONAL("nacional"),
    MUNICIPAL("municipal"),
    TRADICIONAL("tradicional"),
    COMEMORATIVO("comemorativo");

    private final String valor;

    TipoFeriado(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}