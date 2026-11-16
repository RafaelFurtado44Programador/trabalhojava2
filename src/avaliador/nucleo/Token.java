package avaliador.nucleo;

/**
 * Unidade lexica da expressao: um algarismo, um operador ou um parentese,
 * acompanhado da posicao que ocupa na cadeia de caracteres original.
 *
 * Guardar a posicao e o que permite ao validador apontar com precisao onde
 * esta o erro quando a expressao e rejeitada.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class Token {

    private final TipoToken tipo;
    private final char simbolo;
    private final int posicao;
    private final double valor;
    private final Operador operador;

    /**
     * Construtor interno. Use as fabricas estaticas para criar tokens.
     */
    private Token(TipoToken tipo, char simbolo, int posicao, double valor, Operador operador) {
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.posicao = posicao;
        this.valor = valor;
        this.operador = operador;
    }

    /**
     * Cria um token de operando a partir de um algarismo.
     *
     * @param algarismo caractere de '0' a '9'
     * @param posicao   posicao na expressao, contada a partir de 1
     */
    public static Token operando(char algarismo, int posicao) {
        return new Token(TipoToken.OPERANDO, algarismo, posicao,
                algarismo - '0', null);
    }

    /**
     * Cria um token de operando a partir de um valor numerico ja calculado.
     * Usado apenas internamente pelo avaliador.
     *
     * @param valor valor numerico
     */
    public static Token operando(double valor) {
        return new Token(TipoToken.OPERANDO, '#', -1, valor, null);
    }

    /**
     * Cria um token de operador.
     *
     * @param operador operador identificado
     * @param posicao  posicao na expressao, contada a partir de 1
     */
    public static Token operador(Operador operador, int posicao) {
        return new Token(TipoToken.OPERADOR, operador.getSimbolo(), posicao,
                0.0, operador);
    }

    /**
     * Cria um token de abertura de parenteses.
     *
     * @param posicao posicao na expressao, contada a partir de 1
     */
    public static Token abreParentese(int posicao) {
        return new Token(TipoToken.ABRE_PARENTESE, '(', posicao, 0.0, null);
    }

    /**
     * Cria um token de fechamento de parenteses.
     *
     * @param posicao posicao na expressao, contada a partir de 1
     */
    public static Token fechaParentese(int posicao) {
        return new Token(TipoToken.FECHA_PARENTESE, ')', posicao, 0.0, null);
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public int getPosicao() {
        return posicao;
    }

    public double getValor() {
        return valor;
    }

    public Operador getOperador() {
        return operador;
    }

    public boolean ehOperando() {
        return tipo == TipoToken.OPERANDO;
    }

    public boolean ehOperador() {
        return tipo == TipoToken.OPERADOR;
    }

    public boolean ehAbreParentese() {
        return tipo == TipoToken.ABRE_PARENTESE;
    }

    public boolean ehFechaParentese() {
        return tipo == TipoToken.FECHA_PARENTESE;
    }

    @Override
    public String toString() {
        return String.valueOf(simbolo);
    }
}
