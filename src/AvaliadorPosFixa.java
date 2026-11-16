public class AvaliadorPosFixa {

    public double avaliar(Fila<Character> posFixa) {
        if (posFixa == null || posFixa.estaVazia()) {
            throw new IllegalArgumentException("Nao ha expressao para avaliar.");
        }

        Fila<Character> entrada = posFixa.copiar();
        Pilha<Double> valores = new Pilha<Double>();

        while (!entrada.estaVazia()) {
            char c = entrada.desenfileirar();

            if (ValidadorPosFixa.ehOperando(c)) {
                valores.empilhar((double) (c - '0'));

            } else if (ValidadorPosFixa.ehOperador(c)) {
                if (valores.tamanho() < 2) {
                    throw new IllegalArgumentException(
                            "Faltam operandos para o operador '" + c + "'.");
                }
                double direita = valores.desempilhar();
                double esquerda = valores.desempilhar();
                valores.empilhar(aplicar(c, esquerda, direita));

            } else {
                throw new IllegalArgumentException(
                        "A forma pos-fixa nao pode conter o simbolo '" + c + "'.");
            }
        }

        if (valores.tamanho() != 1) {
            throw new IllegalArgumentException(
                    "Sobraram operandos sem operador correspondente.");
        }

        return valores.desempilhar();
    }

    public static double aplicar(char operador, double esquerda, double direita) {
        switch (operador) {
            case '+':
                return esquerda + direita;

            case '-':
                return esquerda - direita;

            case '*':
                return esquerda * direita;

            case '/':
                if (direita == 0.0) {
                    throw new ArithmeticException(
                            "Divisao por zero durante a avaliacao da expressao.");
                }
                return esquerda / direita;

            case '^':
                double potencia = Math.pow(esquerda, direita);
                if (Double.isNaN(potencia) || Double.isInfinite(potencia)) {
                    throw new ArithmeticException("Exponenciacao com resultado indefinido: "
                            + esquerda + " ^ " + direita);
                }
                return potencia;

            default:
                throw new IllegalArgumentException("Operador desconhecido: " + operador);
        }
    }

    public static String formatar(double valor) {
        if (Double.isNaN(valor)) {
            return "indefinido";
        }
        if (Double.isInfinite(valor)) {
            return (valor > 0) ? "+infinito" : "-infinito";
        }
        if (Math.abs(valor) >= 1e15) {
            return Double.toString(valor);
        }

        double arredondado = Math.round(valor * 1000000.0) / 1000000.0;
        if (arredondado == Math.floor(arredondado)) {
            return String.valueOf((long) arredondado);
        }
        return Double.toString(arredondado);
    }
}
