package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

/**
 * Terceira etapa do processamento: converte a expressao da forma infixa para a
 * forma pos-fixa (notacao polonesa inversa).
 *
 * O algoritmo utilizado e o do patio de manobras (shunting-yard), que percorre
 * os tokens da esquerda para a direita uma unica vez, custo O(n), apoiado em
 * uma pilha de operadores e produzindo uma fila de saida.
 *
 * Regras aplicadas a cada token lido:
 *
 *   OPERANDO          -> vai direto para a fila de saida.
 *
 *   OPERADOR          -> enquanto o topo da pilha for um operador cuja
 *                        precedencia justifique sair antes, desempilha para a
 *                        saida; depois empilha o operador lido. O criterio de
 *                        saida depende da associatividade:
 *                          esquerda: sai enquanto topo.precedencia >= atual
 *                          direita:  sai enquanto topo.precedencia >  atual
 *                        E isso que faz 2^3^2 ser lido como 2^(3^2) e
 *                        8-3-2 ser lido como (8-3)-2.
 *
 *   ABRE_PARENTESE    -> empilha, funcionando como marcador de fronteira.
 *
 *   FECHA_PARENTESE   -> desempilha para a saida ate encontrar o abre
 *                        correspondente, que e descartado. Os parenteses nao
 *                        aparecem na forma pos-fixa.
 *
 * Ao final, o que restou na pilha e transferido para a saida.
 *
 * A expressao recebida ja deve ter passado pelo ValidadorExpressao.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class ConversorPosFixa {

    /**
     * Converte a fila de tokens da forma infixa para a forma pos-fixa.
     * A fila recebida nao e consumida.
     *
     * @param tokensInfixa fila de tokens em ordem infixa
     * @return nova fila de tokens em ordem pos-fixa
     * @throws ExpressaoInvalidaException se houver parenteses desbalanceados
     */
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

    /**
     * Decide se o operador que esta no topo da pilha deve sair antes de o
     * operador atual entrar.
     *
     * @param topo  operador no topo da pilha
     * @param atual operador recem-lido da entrada
     * @return true se o topo deve ser desempilhado para a saida
     */
    private boolean deveDesempilhar(Operador topo, Operador atual) {
        if (atual.getAssociatividade() == Associatividade.ESQUERDA) {
            return topo.getPrecedencia() >= atual.getPrecedencia();
        }
        return topo.getPrecedencia() > atual.getPrecedencia();
    }
}
