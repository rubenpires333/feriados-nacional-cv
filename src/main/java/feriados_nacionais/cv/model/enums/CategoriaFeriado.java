package feriados_nacionais.cv.model.enums;

public enum CategoriaFeriado {
    CIVIL("civil"),
    CIVICO("civico"),
    RELIGIOSO("religioso"),
    RELIGIOSO_CIVIL("religioso_civil"),
    POPULAR_CULTURAL("popular_cultural"),
    POPULAR_RELIGIOSO("popular_religioso");

    private final String valor;

    CategoriaFeriado(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}