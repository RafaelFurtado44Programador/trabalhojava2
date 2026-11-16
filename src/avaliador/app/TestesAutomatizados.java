package avaliador.app;

import avaliador.nucleo.AvaliadorExpressoes;
import avaliador.nucleo.ExpressaoInvalidaException;

/**
 * Bateria de testes de aceitacao do avaliador.
 *
 * Os casos de conversao reproduzem as duas tabelas do enunciado do trabalho.
 * Como o programa so aceita operandos de um algarismo, as variaveis das
 * tabelas foram substituidas pela sua posicao no alfabeto:
 *
 *   A=1  B=2  C=3  D=4  E=5  F=6  G=7  H=8
 *
 * A substituicao preserva integralmente a ESTRUTURA da conversao, que e o que
 * os casos pretendem verificar.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class TestesAutomatizados {

    private int total;
    private int aprovados;
    private final AvaliadorExpressoes avaliador = new AvaliadorExpressoes();

    /**
     * Executa todos os grupos de teste e imprime o relatorio.
     *
     * @return true se todos os casos passaram
     */
    public boolean executar() {
        total = 0;
        aprovados = 0;

        System.out.println();
        System.out.println("===========================================================");
        System.out.println("  BATERIA DE TESTES AUTOMATIZADOS");
        System.out.println("===========================================================");

        testarConversaoTabela1();
        testarConversaoTabela2();
        testarPrecedenciaEAssociatividade();
        testarAvaliacaoNumerica();
        testarExpressoesInvalidas();

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("  RESULTADO: " + aprovados + " de " + total + " casos aprovados.");
        if (aprovados == total) {
            System.out.println("  TODOS OS TESTES PASSARAM.");
        } else {
            System.out.println("  ATENCAO: " + (total - aprovados) + " caso(s) falharam.");
        }
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        return aprovados == total;
    }

    /**
     * Primeira tabela de conversao do enunciado.
     */
    private void testarConversaoTabela1() {
        titulo("Conversao infixa -> pos-fixa (tabela 1 do enunciado)");

        conversao("1+2", "1 2 +", "A+B  ->  AB+");
        conversao("1+2-3", "1 2 + 3 -", "A+B-C  ->  AB+C-");
        conversao("(1+2)*(3-4)", "1 2 + 3 4 - *", "(A+B)*(C-D)  ->  AB+CD-*");
        conversao("1^2*3-4+5/6/(7+8)", "1 2 ^ 3 * 4 - 5 6 / 7 8 + / +",
                "A^B*C-D+E/F/(G+H)  ->  AB^C*D-EF/GH+/+");
        conversao("((1+2)*3-(4-5))^(6+7)", "1 2 + 3 * 4 5 - - 6 7 + ^",
                "((A+B)*C-(D-E))^(F+G)  ->  AB+C*DE--FG+^");
        conversao("1-2/(3*4^5)", "1 2 3 4 5 ^ * / -", "A-B/(C*D^E)  ->  ABCDE^*/-");
    }

    /**
     * Segunda tabela do enunciado: o papel dos parenteses.
     */
    private void testarConversaoTabela2() {
        titulo("Conversao infixa -> pos-fixa (tabela 2 do enunciado)");

        conversao("1+(2*3)", "1 2 3 * +", "A+(B*C)  ->  ABC*+");
        conversao("(1+2)*3", "1 2 + 3 *", "(A+B)*C  ->  AB+C*");
        conversao("1+2*3", "1 2 3 * +", "A+B*C equivale a A+(B*C)");
    }

    /**
     * Regras de precedencia e de associatividade descritas no enunciado.
     */
    private void testarPrecedenciaEAssociatividade() {
        titulo("Precedencia e associatividade");

        conversao("1+2+3", "1 2 + 3 +", "A+B+C equivale a (A+B)+C  [esquerda]");
        conversao("2^3^2", "2 3 2 ^ ^", "A^B^C equivale a A^(B^C)  [direita]");
        conversao("8-3-2", "8 3 - 2 -", "subtracao associa a esquerda");
        conversao("8/4/2", "8 4 / 2 /", "divisao associa a esquerda");
        conversao("2*3^2", "2 3 2 ^ *", "exponenciacao antes da multiplicacao");

        valor("2^3^2", 512.0, "2^(3^2) = 2^9 = 512, e nao (2^3)^2 = 64");
        valor("8-3-2", 3.0, "(8-3)-2 = 3, e nao 8-(3-2) = 7");
        valor("8/4/2", 1.0, "(8/4)/2 = 1, e nao 8/(4/2) = 4");
    }

    /**
     * Avaliacao numerica, incluindo o exemplo citado no enunciado.
     */
    private void testarAvaliacaoNumerica() {
        titulo("Avaliacao numerica");

        valor("1+2", 3.0, "soma simples");
        valor("9-4", 5.0, "subtracao simples");
        valor("6*7", 42.0, "multiplicacao simples");
        valor("8/2", 4.0, "divisao exata");
        valor("3^2", 9.0, "exponenciacao: 3^2 = 9 (exemplo do enunciado)");
        valor("2+3*4", 14.0, "precedencia: 2+(3*4)");
        valor("(2+3)*4", 20.0, "parenteses alteram a precedencia");
        valor("(2+5*8)/(4+3)", 6.0, "exemplo do enunciado com o agrupamento explicito");
        valor("2+5*8/(4+3)", 2.0 + 40.0 / 7.0, "mesmo exemplo pela precedencia padrao");
        valor("1+2*3-4/2", 5.0, "mistura de precedencias");
        valor("((1+2))", 3.0, "parenteses redundantes");
        valor("9/2", 4.5, "divisao com resultado fracionario");
        valor("(3-5)^2", 4.0, "base negativa com expoente par");
    }

    /**
     * Expressoes que devem ser recusadas pelo validador.
     */
    private void testarExpressoesInvalidas() {
        titulo("Deteccao de expressoes invalidas");

        invalida("(3(+5))", "exemplo do enunciado: parenteses em posicao invalida");
        invalida("1+", "falta o operando da direita");
        invalida("+1", "falta o operando da esquerda");
        invalida("1++2", "dois operadores seguidos");
        invalida("12", "dois operandos seguidos sem operador");
        invalida("(1+2", "abre-parenteses sem fechamento");
        invalida("1+2)", "fecha-parenteses sem abertura");
        invalida("()", "parenteses vazios");
        invalida("1+2$3", "caractere fora do alfabeto");
        invalida("", "expressao vazia");
        invalida("   ", "expressao composta apenas de espacos");
        invalida("(1+2)(3+4)", "falta operador entre os grupos");
        invalida("1/0", "divisao por zero");
    }

    // ------------------------------------------------------------------
    //  Metodos de apoio
    // ------------------------------------------------------------------

    private void titulo(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " ---");
    }

    private void conversao(String expressao, String esperado, String descricao) {
        total++;
        try {
            String obtido = avaliador.converterParaPosFixa(expressao);
            if (obtido.equals(esperado)) {
                aprovados++;
                System.out.println("  [OK]    " + preencher(expressao) + " -> " + obtido);
            } else {
                System.out.println("  [FALHA] " + preencher(expressao)
                        + " -> " + obtido + "   (esperado: " + esperado + ")");
            }
        } catch (ExpressaoInvalidaException e) {
            System.out.println("  [FALHA] " + preencher(expressao)
                    + " -> recusada: " + e.getMessage());
        }
        System.out.println("          " + descricao);
    }

    private void valor(String expressao, double esperado, String descricao) {
        total++;
        try {
            double obtido = avaliador.avaliar(expressao);
            if (Math.abs(obtido - esperado) < 1e-9) {
                aprovados++;
                System.out.println("  [OK]    " + preencher(expressao) + " = "
                        + AvaliadorExpressoes.formatar(obtido));
            } else {
                System.out.println("  [FALHA] " + preencher(expressao) + " = "
                        + AvaliadorExpressoes.formatar(obtido)
                        + "   (esperado: " + AvaliadorExpressoes.formatar(esperado) + ")");
            }
        } catch (ExpressaoInvalidaException e) {
            System.out.println("  [FALHA] " + preencher(expressao)
                    + " -> recusada: " + e.getMessage());
        }
        System.out.println("          " + descricao);
    }

    private void invalida(String expressao, String descricao) {
        total++;
        String rotulo = expressao.isEmpty() ? "(vazia)" : expressao;
        try {
            double obtido = avaliador.avaliar(expressao);
            System.out.println("  [FALHA] " + preencher(rotulo)
                    + " foi aceita e resultou em "
                    + AvaliadorExpressoes.formatar(obtido));
        } catch (ExpressaoInvalidaException e) {
            aprovados++;
            String posicao = e.temPosicao() ? " (posicao " + e.getPosicao() + ")" : "";
            System.out.println("  [OK]    " + preencher(rotulo)
                    + " recusada" + posicao);
        }
        System.out.println("          " + descricao);
    }

    private String preencher(String texto) {
        StringBuilder sb = new StringBuilder(texto);
        while (sb.length() < 24) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
