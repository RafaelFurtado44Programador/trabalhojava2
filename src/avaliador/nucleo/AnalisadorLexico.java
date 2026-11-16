package avaliador.nucleo;

import avaliador.estruturas.Fila;

public class AnalisadorLexico {
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
