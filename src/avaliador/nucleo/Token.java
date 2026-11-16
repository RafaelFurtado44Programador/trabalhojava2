package avaliador.nucleo;

public class Token {
    private final TipoToken tipo;
    private final char simbolo;
    private final int posicao;
    private final double valor;
    private final Operador operador;

    private Token(TipoToken tipo, char simbolo, int posicao, double valor, Operador operador) {
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.posicao = posicao;
        this.valor = valor;
        this.operador = operador;
    }

    public static Token operando(char algarismo, int posicao) {
        return new Token(TipoToken.OPERANDO, algarismo, posicao,
                algarismo - '0', null);
    }

    public static Token operando(double valor) {
        return new Token(TipoToken.OPERANDO, '#', -1, valor, null);
    }

    public static Token operador(Operador operador, int posicao) {
        return new Token(TipoToken.OPERADOR, operador.getSimbolo(), posicao,
                0.0, operador);
    }

    public static Token abreParentese(int posicao) {
        return new Token(TipoToken.ABRE_PARENTESE, '(', posicao, 0.0, null);
    }

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
