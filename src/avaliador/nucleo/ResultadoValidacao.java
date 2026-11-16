package avaliador.nucleo;

public class ResultadoValidacao {
    private final boolean valida;
    private final String mensagem;
    private final int posicao;

    private ResultadoValidacao(boolean valida, String mensagem, int posicao) {
        this.valida = valida;
        this.mensagem = mensagem;
        this.posicao = posicao;
    }

    public static ResultadoValidacao valida() {
        return new ResultadoValidacao(true, "Expressao sintaticamente valida.", -1);
    }

    public static ResultadoValidacao invalida(String mensagem, int posicao) {
        return new ResultadoValidacao(false, mensagem, posicao);
    }

    public static ResultadoValidacao invalida(String mensagem) {
        return new ResultadoValidacao(false, mensagem, -1);
    }

    public boolean ehValida() {
        return valida;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getPosicao() {
        return posicao;
    }

    public boolean temPosicao() {
        return posicao > 0;
    }

    @Override
    public String toString() {
        if (valida) {
            return mensagem;
        }
        if (temPosicao()) {
            return mensagem + " (posicao " + posicao + ")";
        }
        return mensagem;
    }
}
