package avaliador.nucleo;

import avaliador.estruturas.Fila;

/**
 * Primeira etapa do processamento: percorre a cadeia de caracteres da
 * expressao e a transforma em uma fila de tokens.
 *
 * Esta etapa cuida apenas do reconhecimento dos simbolos isolados. Nenhuma
 * regra de ordenacao e verificada aqui -- isso e tarefa do ValidadorExpressao.
 * O analisador rejeita somente caracteres que nao pertencem ao alfabeto da
 * linguagem.
 *
 * Alfabeto aceito:
 *   algarismos  0 1 2 3 4 5 6 7 8 9
 *   operadores  + - * / ^
 *   parenteses  ( )
 *   espacos em branco (ignorados)
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class AnalisadorLexico {

    /**
     * Converte a expressao em uma fila de tokens, preservando a ordem de
     * leitura da esquerda para a direita.
     *
     * @param expressao cadeia de caracteres digitada pelo usuario
     * @return fila de tokens na ordem em que aparecem
     * @throws ExpressaoInvalidaException se a expressao for nula, vazia ou
     *                                    contiver caractere fora do alfabeto
     */
    public Fila<Token> analisar(String expressao) {
        if (expressao == null) {
            throw new ExpressaoInvalidaException("A expressao nao pode ser nula.");
        }

        Fila<Token> tokens = new Fila<Token>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            int posicao = i + 1;

            if (c == ' ' || c == '\t') {
                continue;
            }

            if (c >= '0' && c <= '9') {
                tokens.enfileirar(Token.operando(c, posicao));

            } else if (Operador.ehOperador(c)) {
                tokens.enfileirar(Token.operador(Operador.porSimbolo(c), posicao));

            } else if (c == '(') {
                tokens.enfileirar(Token.abreParentese(posicao));

            } else if (c == ')') {
                tokens.enfileirar(Token.fechaParentese(posicao));

            } else {
                throw new ExpressaoInvalidaException(
                        "Caractere invalido '" + c + "'. Sao aceitos apenas "
                                + "algarismos de 0 a 9, os operadores + - * / ^ "
                                + "e os parenteses.", posicao);
            }
        }

        if (tokens.estaVazia()) {
            throw new ExpressaoInvalidaException("A expressao esta vazia.");
        }

        return tokens;
    }
}
