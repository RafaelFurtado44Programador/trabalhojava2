package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

/**
 * Quarta e ultima etapa do processamento: calcula o valor numerico de uma
 * expressao ja em notacao pos-fixa.
 *
 * O algoritmo e direto e percorre a fila uma unica vez, custo O(n):
 *
 *   para cada token da fila pos-fixa:
 *     se for operando -> empilha o seu valor
 *     se for operador -> desempilha dois valores, aplica a operacao e
 *                        empilha o resultado
 *
 * Atencao a ordem dos operandos: o PRIMEIRO valor desempilhado e o operando da
 * DIREITA e o SEGUNDO e o da ESQUERDA. Inverter essa ordem quebraria a
 * subtracao, a divisao e a exponenciacao.
 *
 * Ao final, a pilha deve conter exatamente um valor, que e o resultado.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class AvaliadorPosFixa {

    /**
     * Calcula o valor da expressao pos-fixa. A fila recebida nao e consumida.
     *
     * @param posFixa fila de tokens em ordem pos-fixa
     * @return valor numerico da expressao
     * @throws ExpressaoInvalidaException se a expressao estiver malformada ou
     *                                    se ocorrer divisao por zero
     */
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
