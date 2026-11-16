public class ConversorPosFixa {

    public Fila<Character> converter(String expressao) {
        if (expressao == null) {
            throw new IllegalArgumentException("Nao ha expressao para converter.");
        }

        Fila<Character> saida = new Fila<Character>();
        Pilha<Character> operadores = new Pilha<Character>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (c == ' ' || c == '\t') {
                continue;
            }

            if (ValidadorPosFixa.ehOperando(c)) {
                saida.enfileirar(c);

            } else if (ValidadorPosFixa.ehOperador(c)) {
                while (!operadores.estaVazia()
                        && ValidadorPosFixa.ehOperador(operadores.espiar())
                        && deveDesempilhar(operadores.espiar(), c)) {
                    saida.enfileirar(operadores.desempilhar());
                }
                operadores.empilhar(c);

            } else if (c == '(') {
                operadores.empilhar(c);

            } else if (c == ')') {
                while (!operadores.estaVazia() && operadores.espiar() != '(') {
                    saida.enfileirar(operadores.desempilhar());
                }
                if (operadores.estaVazia()) {
                    throw new IllegalArgumentException(
                            "Fecha-parenteses sem o abre-parenteses correspondente.");
                }
                operadores.desempilhar();

            } else {
                throw new IllegalArgumentException("Caractere invalido '" + c + "'.");
            }
        }

        while (!operadores.estaVazia()) {
            char restante = operadores.desempilhar();
            if (restante == '(') {
                throw new IllegalArgumentException(
                        "Abre-parenteses sem o fecha-parenteses correspondente.");
            }
            saida.enfileirar(restante);
        }

        return saida;
    }

    public String converterParaTexto(String expressao) {
        return converter(expressao).toString();
    }

    private boolean deveDesempilhar(char topo, char atual) {
        if (associaDireita(atual)) {
            return precedencia(topo) > precedencia(atual);
        }
        return precedencia(topo) >= precedencia(atual);
    }

    public static int precedencia(char operador) {
        if (operador == '^') {
            return 3;
        }
        if (operador == '*' || operador == '/') {
            return 2;
        }
        if (operador == '+' || operador == '-') {
            return 1;
        }
        return 0;
    }

    public static boolean associaDireita(char operador) {
        return operador == '^';
    }
}
