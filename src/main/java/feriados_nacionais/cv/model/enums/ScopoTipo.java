package feriados_nacionais.cv.model.enums;

public enum ScopoTipo {
    NACIONAL("nacional"),
    ILHA("ilha"),
    MUNICIPIO("municipio");

    private final String valor;

    ScopoTipo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}