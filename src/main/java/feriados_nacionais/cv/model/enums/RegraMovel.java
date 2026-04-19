package feriados_nacionais.cv.model.enums;

public enum RegraMovel {
    PASCOA_MENOS_47("pascoa_menos_47", -47, "Carnaval (Terça-Feira Gorda)"),
    PASCOA_MENOS_46("pascoa_menos_46", -46, "Quarta-Feira de Cinzas"),
    PASCOA_MENOS_7("pascoa_menos_7", -7, "Domingo de Ramos (início da Semana Santa)"),
    PASCOA_MENOS_2("pascoa_menos_2", -2, "Sexta-Feira Santa"),
    PASCOA("pascoa", 0, "Páscoa"),
    PASCOA_MAIS_39("pascoa_mais_39", 39, "Ascensão"),
    PASCOA_MAIS_49("pascoa_mais_49", 49, "Pentecostes");

    private final String valor;
    private final int diasDaPascoa;
    private final String descricao;

    RegraMovel(String valor, int diasDaPascoa, String descricao) {
        this.valor = valor;
        this.diasDaPascoa = diasDaPascoa;
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public int getDiasDaPascoa() {
        return diasDaPascoa;
    }

    public String getDescricao() {
        return descricao;
    }
}