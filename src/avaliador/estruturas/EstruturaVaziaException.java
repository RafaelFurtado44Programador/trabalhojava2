package avaliador.estruturas;

/**
 * Lancada quando se tenta remover ou consultar um elemento de uma estrutura
 * (Pilha ou Fila) que nao possui elementos.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class EstruturaVaziaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstruturaVaziaException(String mensagem) {
        super(mensagem);
    }
}
