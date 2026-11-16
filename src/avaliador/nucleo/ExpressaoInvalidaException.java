package avaliador.nucleo;

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

    public int getPosicao() {
        return posicao;
    }

    public boolean temPosicao() {
        return posicao > 0;
    }
}
