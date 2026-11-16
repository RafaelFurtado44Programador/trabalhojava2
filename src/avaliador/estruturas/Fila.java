package avaliador.estruturas;

/**
 * Fila generica (FIFO - First In, First Out) implementada por encadeamento
 * simples, sem qualquer uso de estruturas prontas da biblioteca Java.
 *
 * A fila mantem referencias para o primeiro e para o ultimo no, de modo que
 * tanto a insercao quanto a remocao custam tempo constante O(1).
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class Fila<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    /**
     * Cria uma fila vazia.
     */
    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    /**
     * Insere um elemento no fim da fila. Custo O(1).
     *
     * @param valor elemento a ser inserido
     */
    public void enfileirar(T valor) {
        No<T> novo = new No<T>(valor);
        if (estaVazia()) {
            inicio = novo;
        } else {
            fim.setProximo(novo);
        }
        fim = novo;
        tamanho++;
    }

    /**
     * Remove e devolve o elemento do inicio da fila. Custo O(1).
     *
     * @return elemento removido
     * @throws EstruturaVaziaException se a fila estiver vazia
     */
    public T desenfileirar() {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Tentativa de desenfileirar de uma fila vazia.");
        }
        T valor = inicio.getValor();
        inicio = inicio.getProximo();
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return valor;
    }

    /**
     * Devolve o elemento do inicio sem remove-lo. Custo O(1).
     *
     * @return elemento do inicio
     * @throws EstruturaVaziaException se a fila estiver vazia
     */
    public T primeiro() {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Tentativa de consultar o inicio de uma fila vazia.");
        }
        return inicio.getValor();
    }

    /**
     * @return true se nao houver elementos na fila
     */
    public boolean estaVazia() {
        return inicio == null;
    }

    /**
     * @return quantidade de elementos armazenados
     */
    public int tamanho() {
        return tamanho;
    }

    /**
     * Remove todos os elementos da fila.
     */
    public void limpar() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    /**
     * Produz uma copia independente desta fila, preservando a ordem. Util para
     * percorrer o conteudo sem consumir a fila original. Custo O(n).
     *
     * @return nova fila com os mesmos elementos, na mesma ordem
     */
    public Fila<T> copiar() {
        Fila<T> copia = new Fila<T>();
        No<T> atual = inicio;
        while (atual != null) {
            copia.enfileirar(atual.getValor());
            atual = atual.getProximo();
        }
        return copia;
    }

    /**
     * Representacao textual do conteudo, do inicio para o fim, com os
     * elementos separados por um espaco em branco.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        No<T> atual = inicio;
        while (atual != null) {
            sb.append(atual.getValor());
            if (atual.getProximo() != null) {
                sb.append(' ');
            }
            atual = atual.getProximo();
        }
        return sb.toString();
    }
}
