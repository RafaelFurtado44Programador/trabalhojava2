package avaliador.nucleo;

/**
 * Catalogo dos cinco operadores binarios aceitos pelo avaliador, com a
 * precedencia e a associatividade de cada um, alem da regra de calculo.
 *
 * Ordem de precedencia adotada (da superior para a inferior), exatamente como
 * descrito no enunciado do trabalho:
 *
 *   3 - Exponenciacao          ^      (associativa a direita)
 *   2 - Multiplicacao/Divisao  *  /   (associativas a esquerda)
 *   1 - Adicao/Subtracao       +  -   (associativas a esquerda)
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
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

    /**
     * Identifica o operador correspondente a um caractere.
     *
     * @param c caractere lido da expressao
     * @return o operador correspondente, ou null se o caractere nao for um
     *         operador valido
     */
    public static Operador porSimbolo(char c) {
        for (Operador op : Operador.values()) {
            if (op.simbolo == c) {
                return op;
            }
        }
        return null;
    }

    /**
     * Verifica se um caractere representa algum dos operadores aceitos.
     *
     * @param c caractere a testar
     * @return true se for operador valido
     */
    public static boolean ehOperador(char c) {
        return porSimbolo(c) != null;
    }

    /**
     * Aplica a operacao sobre dois operandos.
     *
     * Atencao a ordem: em notacao pos-fixa, o primeiro valor desempilhado e o
     * operando da DIREITA e o segundo e o da ESQUERDA. Esta funcao ja recebe
     * os valores na ordem correta da expressao original.
     *
     * @param esquerda operando a esquerda do operador
     * @param direita  operando a direita do operador
     * @return resultado da operacao
     * @throws ExpressaoInvalidaException em caso de divisao por zero ou de
     *                                    resultado matematicamente indefinido
     */
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
