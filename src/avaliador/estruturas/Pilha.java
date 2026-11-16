package avaliador.estruturas;

/**
 * Pilha generica (LIFO - Last In, First Out) implementada por encadeamento
 * simples, sem qualquer uso de estruturas prontas da biblioteca Java.
 *
 * O topo da pilha e sempre o primeiro no da cadeia, de modo que as operacoes
 * de empilhar e desempilhar custam tempo constante O(1).
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class Pilha<T> {

    private No<T> topo;
    private int tamanho;

    /**
     * Cria uma pilha vazia.
     */
    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }

    /**
     * Insere um elemento no topo da pilha. Custo O(1).
     *
     * @param valor elemento a ser inserido
     */
    public void empilhar(T valor) {
        topo = new No<T>(valor, topo);
        tamanho++;
    }

    /**
     * Remove e devolve o elemento do topo da pilha. Custo O(1).
     *
     * @return elemento removido
     * @throws EstruturaVaziaException se a pilha estiver vazia
     */
    public T desempilhar() {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Tentativa de desempilhar de uma pilha vazia.");
        }
        T valor = topo.getValor();
        topo = topo.getProximo();
        tamanho--;
        return valor;
    }

    /**
     * Devolve o elemento do topo sem remove-lo. Custo O(1).
     *
     * @return elemento do topo
     * @throws EstruturaVaziaException se a pilha estiver vazia
     */
    public T espiar() {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Tentativa de consultar o topo de uma pilha vazia.");
        }
        return topo.getValor();
    }

    /**
     * @return true se nao houver elementos na pilha
     */
    public boolean estaVazia() {
        return topo == null;
    }

    /**
     * @return quantidade de elementos armazenados
     */
    public int tamanho() {
        return tamanho;
    }

    /**
     * Remove todos os elementos da pilha.
     */
    public void limpar() {
        topo = null;
        tamanho = 0;
    }

    /**
     * Representacao textual do conteudo, do topo para a base.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[topo] ");
        No<T> atual = topo;
        while (atual != null) {
            sb.append(atual.getValor());
            if (atual.getProximo() != null) {
                sb.append(' ');
            }
            atual = atual.getProximo();
        }
        sb.append(" [base]");
        return sb.toString();
    }
}
