package avaliador.nucleo;

/**
 * Lancada quando a expressao fornecida nao pode ser processada, seja por erro
 * de sintaxe, seja por erro surgido durante a avaliacao numerica (divisao por
 * zero, por exemplo).
 *
 * Quando o erro possui uma posicao conhecida dentro da cadeia de caracteres da
 * expressao, essa posicao fica registrada no atributo {@code posicao}, contada
 * a partir de 1. Quando a posicao nao se aplica, o atributo vale -1.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class ExpressaoInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int posicao;

    public ExpressaoInvalidaException(String mensagem) {
        super(mensagem);
        this.posicao = -1;
    }

    public ExpressaoInvalidaException(String mensagem, int posicao) {
        super(mensagem);
        this.posicao = posicao;
    }

    /**
     * @return posicao do erro na expressao (a partir de 1), ou -1 se nao se
     *         aplica
     */
    public int getPosicao() {
        return posicao;
    }

    /**
     * @return true se o erro possui uma posicao conhecida na expressao
     */
    public boolean temPosicao() {
        return posicao > 0;
    }
}
