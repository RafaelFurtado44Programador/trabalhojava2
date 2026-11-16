package avaliador.nucleo;

/**
 * Resposta produzida pelo ValidadorExpressao.
 *
 * Quando a expressao e valida, apenas o campo {@code valida} importa. Quando
 * nao e, a mensagem explica a causa e a posicao indica em que ponto da cadeia
 * de caracteres o problema foi detectado.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class ResultadoValidacao {

    private final boolean valida;
    private final String mensagem;
    private final int posicao;

    private ResultadoValidacao(boolean valida, String mensagem, int posicao) {
        this.valida = valida;
        this.mensagem = mensagem;
        this.posicao = posicao;
    }

    /**
     * Fabrica um resultado positivo.
     */
    public static ResultadoValidacao valida() {
        return new ResultadoValidacao(true, "Expressao sintaticamente valida.", -1);
    }

    /**
     * Fabrica um resultado negativo com posicao conhecida.
     *
     * @param mensagem explicacao do erro
     * @param posicao  posicao na expressao, a partir de 1
     */
    public static ResultadoValidacao invalida(String mensagem, int posicao) {
        return new ResultadoValidacao(false, mensagem, posicao);
    }

    /**
     * Fabrica um resultado negativo sem posicao especifica.
     *
     * @param mensagem explicacao do erro
     */
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
