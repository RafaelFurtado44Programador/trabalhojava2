package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

public class ConversorPosFixa {
    public Fila<Token> converter(Fila<Token> tokensInfixa) {
        if (tokensInfixa == null || tokensInfixa.estaVazia()) {
            throw new ExpressaoInvalidaException("Nao ha expressao para converter.");
        }

        Fila<Token> entrada = tokensInfixa.copiar();
        Fila<Token> saida = new Fila<Token>();
        Pilha<Token> operadores = new Pilha<Token>();

        while (!entrada.estaVazia()) {
            Token token = entrada.desenfileirar();

            switch (token.getTipo()) {
                case OPERANDO:
                    saida.enfileirar(token);
                    break;

                case OPERADOR:
                    while (!operadores.estaVazia()
                            && operadores.espiar().ehOperador()
                            && deveDesempilhar(operadores.espiar().getOperador(),
                                               token.getOperador())) {
                        saida.enfileirar(operadores.desempilhar());
                    }
                    operadores.empilhar(token);
                    break;

                case ABRE_PARENTESE:
                    operadores.empilhar(token);
                    break;

                case FECHA_PARENTESE:
                    while (!operadores.estaVazia() && !operadores.espiar().ehAbreParentese()) {
                        saida.enfileirar(operadores.desempilhar());
                    }
                    if (operadores.estaVazia()) {
                        throw new ExpressaoInvalidaException(
                                "Fecha-parenteses sem o abre-parenteses correspondente.",
                                token.getPosicao());
                    }
                    operadores.desempilhar();
                    break;

                default:
                    throw new ExpressaoInvalidaException(
                            "Simbolo nao reconhecido durante a conversao: '"
                                    + token.getSimbolo() + "'.",
                            token.getPosicao());
            }
        }

        while (!operadores.estaVazia()) {
            Token restante = operadores.desempilhar();
            if (restante.ehAbreParentese()) {
                throw new ExpressaoInvalidaException(
                        "Abre-parenteses sem o fecha-parenteses correspondente.",
                        restante.getPosicao());
            }
            saida.enfileirar(restante);
        }

        return saida;
    }

    private boolean deveDesempilhar(Operador topo, Operador atual) {
        if (atual.getAssociatividade() == Associatividade.ESQUERDA) {
            return topo.getPrecedencia() >= atual.getPrecedencia();
        }
        return topo.getPrecedencia() > atual.getPrecedencia();
    }
}
