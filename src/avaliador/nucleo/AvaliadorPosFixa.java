package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

public class AvaliadorPosFixa {
    public double avaliar(Fila<Token> posFixa) {
        if (posFixa == null || posFixa.estaVazia()) {
            throw new ExpressaoInvalidaException("Nao ha expressao para avaliar.");
        }

        Fila<Token> entrada = posFixa.copiar();
        Pilha<Double> valores = new Pilha<Double>();

        while (!entrada.estaVazia()) {
            Token token = entrada.desenfileirar();

            if (token.ehOperando()) {
                valores.empilhar(token.getValor());

            } else if (token.ehOperador()) {
                if (valores.tamanho() < 2) {
                    throw new ExpressaoInvalidaException(
                            "Faltam operandos para o operador '"
                                    + token.getSimbolo() + "'.",
                            token.getPosicao());
                }
                double direita = valores.desempilhar();
                double esquerda = valores.desempilhar();
                valores.empilhar(token.getOperador().aplicar(esquerda, direita));

            } else {
                throw new ExpressaoInvalidaException(
                        "A forma pos-fixa nao pode conter parenteses.",
                        token.getPosicao());
            }
        }

        if (valores.tamanho() != 1) {
            throw new ExpressaoInvalidaException(
                    "Sobraram operandos sem operador correspondente.");
        }

        return valores.desempilhar();
    }
}
