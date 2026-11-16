package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

/**
 * Segunda etapa do processamento: verifica se a sequencia de tokens forma uma
 * expressao infixa sintaticamente valida.
 *
 * A verificacao e feita por um automato de dois estados que percorre a fila de
 * tokens uma unica vez, custo O(n):
 *
 *   ESPERANDO_OPERANDO -- so pode vir um algarismo ou um '('
 *   ESPERANDO_OPERADOR -- so pode vir um operador ou um ')'
 *
 * Transicoes:
 *   algarismo -> ESPERANDO_OPERADOR
 *   '('       -> ESPERANDO_OPERANDO   (e empilha o parentese)
 *   operador  -> ESPERANDO_OPERANDO
 *   ')'       -> ESPERANDO_OPERADOR   (e desempilha o parentese correspondente)
 *
 * Ao final, a expressao so e valida se o automato parar em ESPERANDO_OPERADOR
 * (ou seja, terminou com um operando ou um fecha-parenteses) e se a pilha de
 * parenteses estiver vazia.
 *
 * Esse automato detecta de uma vez os tres problemas exigidos pelo enunciado:
 * numero incorreto de operandos, de operadores e de parenteses, alem da
 * posicao incorreta de qualquer um deles. O exemplo do enunciado, ( 3 ( + 5 ) ),
 * e rejeitado porque o segundo '(' aparece quando o automato esta em
 * ESPERANDO_OPERADOR.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class ValidadorExpressao {

    /** Estados do automato de validacao. */
    private enum Estado {
        ESPERANDO_OPERANDO,
        ESPERANDO_OPERADOR
    }

    /**
     * Verifica a validade sintatica da expressao representada pela fila de
     * tokens. A fila recebida nao e consumida.
     *
     * @param tokens fila produzida pelo AnalisadorLexico
     * @return resultado da validacao, com mensagem e posicao em caso de erro
     */
    public ResultadoValidacao validar(Fila<Token> tokens) {
        if (tokens == null || tokens.estaVazia()) {
            return ResultadoValidacao.invalida("A expressao esta vazia.");
        }

        Fila<Token> fila = tokens.copiar();
        Pilha<Token> parenteses = new Pilha<Token>();
        Estado estado = Estado.ESPERANDO_OPERANDO;
        Token ultimo = null;

        while (!fila.estaVazia()) {
            Token token = fila.desenfileirar();
            ultimo = token;

            switch (token.getTipo()) {

                case OPERANDO:
                    if (estado != Estado.ESPERANDO_OPERANDO) {
                        return ResultadoValidacao.invalida(
                                "Operando '" + token.getSimbolo() + "' em posicao indevida: "
                                        + "era esperado um operador ou um fecha-parenteses.",
                                token.getPosicao());
                    }
                    estado = Estado.ESPERANDO_OPERADOR;
                    break;

                case OPERADOR:
                    if (estado != Estado.ESPERANDO_OPERADOR) {
                        return ResultadoValidacao.invalida(
                                "Operador '" + token.getSimbolo() + "' em posicao indevida: "
                                        + "era esperado um operando ou um abre-parenteses. "
                                        + "Lembre-se de que as cinco operacoes sao binarias.",
                                token.getPosicao());
                    }
                    estado = Estado.ESPERANDO_OPERANDO;
                    break;

                case ABRE_PARENTESE:
                    if (estado != Estado.ESPERANDO_OPERANDO) {
                        return ResultadoValidacao.invalida(
                                "Abre-parenteses em posicao indevida: era esperado um "
                                        + "operador ou um fecha-parenteses. Falta um operador "
                                        + "antes do parenteses.",
                                token.getPosicao());
                    }
                    parenteses.empilhar(token);
                    estado = Estado.ESPERANDO_OPERANDO;
                    break;

                case FECHA_PARENTESE:
                    if (estado != Estado.ESPERANDO_OPERADOR) {
                        return ResultadoValidacao.invalida(
                                "Fecha-parenteses em posicao indevida: era esperado um "
                                        + "operando ou um abre-parenteses. Nao sao admitidos "
                                        + "parenteses vazios nem operador sem o segundo operando.",
                                token.getPosicao());
                    }
                    if (parenteses.estaVazia()) {
                        return ResultadoValidacao.invalida(
                                "Fecha-parenteses sem o abre-parenteses correspondente.",
                                token.getPosicao());
                    }
                    parenteses.desempilhar();
                    estado = Estado.ESPERANDO_OPERADOR;
                    break;

                default:
                    return ResultadoValidacao.invalida(
                            "Simbolo nao reconhecido: '" + token.getSimbolo() + "'.",
                            token.getPosicao());
            }
        }

        if (!parenteses.estaVazia()) {
            Token aberto = parenteses.espiar();
            return ResultadoValidacao.invalida(
                    "Abre-parenteses sem o fecha-parenteses correspondente. "
                            + "Ha " + parenteses.tamanho() + " parenteses em aberto.",
                    aberto.getPosicao());
        }

        if (estado != Estado.ESPERANDO_OPERADOR) {
            int posicao = (ultimo != null) ? ultimo.getPosicao() : -1;
            return ResultadoValidacao.invalida(
                    "A expressao termina de forma incompleta: falta o operando "
                            + "a direita do ultimo operador.",
                    posicao);
        }

        return ResultadoValidacao.valida();
    }
}
