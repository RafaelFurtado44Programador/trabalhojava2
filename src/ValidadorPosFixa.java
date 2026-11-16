public class ValidadorPosFixa {

    private String mensagem;
    private int posicao;

    public ValidadorPosFixa() {
        this.mensagem = null;
        this.posicao = -1;
    }

    public boolean validar(String expressao) {
        mensagem = null;
        posicao = -1;

        if (expressao == null) {
            return registrarErro("A expressao nao pode ser nula.", -1);
        }

        Pilha<Integer> parenteses = new Pilha<Integer>();
        boolean esperandoOperando = true;
        boolean encontrouAlgoAlem = false;
        int ultimaPosicao = -1;

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            int pos = i + 1;

            if (c == ' ' || c == '\t') {
                continue;
            }

            encontrouAlgoAlem = true;
            ultimaPosicao = pos;

            if (ehOperando(c)) {
                if (!esperandoOperando) {
                    return registrarErro("Operando '" + c + "' em posicao indevida: era "
                            + "esperado um operador ou um fecha-parenteses.", pos);
                }
                esperandoOperando = false;

            } else if (ehOperador(c)) {
                if (esperandoOperando) {
                    return registrarErro("Operador '" + c + "' em posicao indevida: era "
                            + "esperado um operando ou um abre-parenteses. Lembre-se de "
                            + "que as cinco operacoes sao binarias.", pos);
                }
                esperandoOperando = true;

            } else if (c == '(') {
                if (!esperandoOperando) {
                    return registrarErro("Abre-parenteses em posicao indevida: era esperado "
                            + "um operador ou um fecha-parenteses. Falta um operador antes "
                            + "do parenteses.", pos);
                }
                parenteses.empilhar(pos);
                esperandoOperando = true;

            } else if (c == ')') {
                if (esperandoOperando) {
                    return registrarErro("Fecha-parenteses em posicao indevida: era esperado "
                            + "um operando ou um abre-parenteses. Nao sao admitidos "
                            + "parenteses vazios nem operador sem o segundo operando.", pos);
                }
                if (parenteses.estaVazia()) {
                    return registrarErro("Fecha-parenteses sem o abre-parenteses "
                            + "correspondente.", pos);
                }
                parenteses.desempilhar();
                esperandoOperando = false;

            } else {
                return registrarErro("Caractere invalido '" + c + "'. Sao aceitos apenas "
                        + "algarismos de 0 a 9, os operadores + - * / ^ e os parenteses.", pos);
            }
        }

        if (!encontrouAlgoAlem) {
            return registrarErro("A expressao esta vazia.", -1);
        }

        if (!parenteses.estaVazia()) {
            return registrarErro("Abre-parenteses sem o fecha-parenteses correspondente. "
                    + "Ha " + parenteses.tamanho() + " parenteses em aberto.",
                    parenteses.espiar());
        }

        if (esperandoOperando) {
            return registrarErro("A expressao termina de forma incompleta: falta o operando "
                    + "a direita do ultimo operador.", ultimaPosicao);
        }

        return true;
    }

    private boolean registrarErro(String texto, int onde) {
        this.mensagem = texto;
        this.posicao = onde;
        return false;
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

    public static boolean ehOperando(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean ehOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
}
