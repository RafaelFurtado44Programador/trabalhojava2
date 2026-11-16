package avaliador.estruturas;

/**
 * No generico de uma estrutura encadeada.
 *
 * Esta classe e a unidade basica de armazenamento utilizada tanto pela Pilha
 * quanto pela Fila implementadas neste pacote. Cada no guarda um valor e uma
 * referencia para o proximo no da cadeia.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class No<T> {

    private T valor;
    private No<T> proximo;

    /**
     * Cria um no isolado, sem sucessor.
     *
     * @param valor conteudo armazenado no no
     */
    public No(T valor) {
        this.valor = valor;
        this.proximo = null;
    }

    /**
     * Cria um no ja encadeado a um sucessor.
     *
     * @param valor   conteudo armazenado no no
     * @param proximo no sucessor na cadeia
     */
    public No(T valor, No<T> proximo) {
        this.valor = valor;
        this.proximo = proximo;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public No<T> getProximo() {
        return proximo;
    }

    public void setProximo(No<T> proximo) {
        this.proximo = proximo;
    }
}
