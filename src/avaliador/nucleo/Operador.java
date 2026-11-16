package avaliador.nucleo;

public enum Operador {
    ADICAO('+', 1, Associatividade.ESQUERDA),
    SUBTRACAO('-', 1, Associatividade.ESQUERDA),
    MULTIPLICACAO('*', 2, Associatividade.ESQUERDA),
    DIVISAO('/', 2, Associatividade.ESQUERDA),
    EXPONENCIACAO('^', 3, Associatividade.DIREITA);

    private final char simbolo;
    private final int precedencia;
    private final Associatividade associatividade;

    Operador(char simbolo, int precedencia, Associatividade associatividade) {
        this.simbolo = simbolo;
        this.precedencia = precedencia;
        this.associatividade = associatividade;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public int getPrecedencia() {
        return precedencia;
    }

    public Associatividade getAssociatividade() {
        return associatividade;
    }

    public static Operador porSimbolo(char c) {
        for (Operador op : Operador.values()) {
            if (op.simbolo == c) {
                return op;
            }
        }
        return null;
    }

    public static boolean ehOperador(char c) {
        return porSimbolo(c) != null;
    }

    public double aplicar(double esquerda, double direita) {
        switch (this) {
            case ADICAO:
                return esquerda + direita;

            case SUBTRACAO:
                return esquerda - direita;

            case MULTIPLICACAO:
                return esquerda * direita;

            case DIVISAO:
                if (direita == 0.0) {
                    throw new ExpressaoInvalidaException(
                            "Divisao por zero durante a avaliacao da expressao.");
                }
                return esquerda / direita;

            case EXPONENCIACAO:
                double potencia = Math.pow(esquerda, direita);
                if (Double.isNaN(potencia) || Double.isInfinite(potencia)) {
                    throw new ExpressaoInvalidaException(
                            "Exponenciacao com resultado indefinido: "
                                    + esquerda + " ^ " + direita);
                }
                return potencia;

            default:
                throw new ExpressaoInvalidaException("Operador desconhecido: " + simbolo);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(simbolo);
    }
}
