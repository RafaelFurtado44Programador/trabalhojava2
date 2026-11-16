public class Pilha<T> {
    private No<T> topo;
    private int tamanho;

    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }

    public void empilhar(T valor) {
        topo = new No<T>(valor, topo);
        tamanho++;
    }

    public T desempilhar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de desempilhar de uma pilha vazia.");
        }
        T valor = topo.getValor();
        topo = topo.getProximo();
        tamanho--;
        return valor;
    }

    public T espiar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de consultar o topo de uma pilha vazia.");
        }
        return topo.getValor();
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public int tamanho() {
        return tamanho;
    }

    public void limpar() {
        topo = null;
        tamanho = 0;
    }

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
