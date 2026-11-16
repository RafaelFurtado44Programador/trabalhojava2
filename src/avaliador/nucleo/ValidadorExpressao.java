package avaliador.nucleo;

import avaliador.estruturas.Fila;
import avaliador.estruturas.Pilha;

public class ValidadorExpressao {
    private enum Estado {
        ESPERANDO_OPERANDO,
        ESPERANDO_OPERADOR
    }

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
