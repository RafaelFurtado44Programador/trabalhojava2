public class Fila<T> {
    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

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

    public T desenfileirar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de desenfileirar de uma fila vazia.");
        }
        T valor = inicio.getValor();
        inicio = inicio.getProximo();
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return valor;
    }

    public T primeiro() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de consultar o inicio de uma fila vazia.");
        }
        return inicio.getValor();
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int tamanho() {
        return tamanho;
    }

    public void limpar() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    public Fila<T> copiar() {
        Fila<T> copia = new Fila<T>();
        No<T> atual = inicio;
        while (atual != null) {
            copia.enfileirar(atual.getValor());
            atual = atual.getProximo();
        }
        return copia;
    }

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
