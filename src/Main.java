import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private final ValidadorPosFixa validador = new ValidadorPosFixa();
    private final ConversorPosFixa conversor = new ConversorPosFixa();
    private final AvaliadorPosFixa avaliador = new AvaliadorPosFixa();

    private final BufferedReader entrada =
            new BufferedReader(new InputStreamReader(System.in));

    private Fila<Character> ultimaPosFixa;

    private int totalTestes;
    private int testesAprovados;

    public static void main(String[] args) {
        Main programa = new Main();

        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
            }
            programa.processarExpressao(sb.toString());
            return;
        }

        programa.executar();
    }

    private double avaliarCompleto(String expressao) {
        if (!validador.validar(expressao)) {
            throw new IllegalArgumentException(validador.getMensagem());
        }
        ultimaPosFixa = conversor.converter(expressao);
        return avaliador.avaliar(ultimaPosFixa);
    }

    private void executar() {
        cabecalho();

        boolean continuar = true;
        while (continuar) {
            menu();
            String opcao = lerLinha("Opcao: ").trim();

            if (opcao.equals("1")) {
                opcaoAvaliar();
            } else if (opcao.equals("2")) {
                opcaoConverter();
            } else if (opcao.equals("3")) {
                opcaoValidar();
            } else if (opcao.equals("4")) {
                executarTestes();
            } else if (opcao.equals("5")) {
                ajuda();
            } else if (opcao.equals("0")) {
                continuar = false;
                System.out.println();
                System.out.println("Encerrando. Ate logo!");
            } else {
                System.out.println();
                System.out.println("Opcao invalida. Escolha um numero de 0 a 5.");
            }
        }
    }

    private void cabecalho() {
        System.out.println();
        System.out.println("===========================================================");
        System.out.println("  AVALIADOR DE EXPRESSOES MATEMATICAS");
        System.out.println("  Trabalho 2 - Lab. AED I - Eng. da Computacao");
        System.out.println("  CEFET-MG - Campus Timoteo");
        System.out.println("===========================================================");
        System.out.println("  Operandos: algarismos de 0 a 9");
        System.out.println("  Operadores: +  -  *  /  ^      Agrupamento: ( )");
        System.out.println("===========================================================");
    }

    private void menu() {
        System.out.println();
        System.out.println("  1 - Avaliar uma expressao");
        System.out.println("  2 - Converter para a forma pos-fixa");
        System.out.println("  3 - Verificar apenas a validade sintatica");
        System.out.println("  4 - Executar a bateria de testes");
        System.out.println("  5 - Ajuda");
        System.out.println("  0 - Sair");
        System.out.println();
    }

    private void opcaoAvaliar() {
        System.out.println();
        processarExpressao(lerLinha("Expressao infixa: "));
    }

    private void processarExpressao(String expressao) {
        System.out.println();
        try {
            double resultado = avaliarCompleto(expressao);
            System.out.println("  Forma infixa  : " + comEspacos(expressao));
            System.out.println("  Forma pos-fixa: " + ultimaPosFixa.toString());
            System.out.println("  Resultado     : " + AvaliadorPosFixa.formatar(resultado));
        } catch (RuntimeException e) {
            relatarErro(expressao, e.getMessage());
        }
    }

    private void opcaoConverter() {
        System.out.println();
        String expressao = lerLinha("Expressao infixa: ");
        System.out.println();
        try {
            if (!validador.validar(expressao)) {
                relatarErro(expressao, validador.getMensagem());
                return;
            }
            System.out.println("  Forma pos-fixa: " + conversor.converterParaTexto(expressao));
        } catch (RuntimeException e) {
            relatarErro(expressao, e.getMessage());
        }
    }

    private void opcaoValidar() {
        System.out.println();
        String expressao = lerLinha("Expressao infixa: ");
        System.out.println();

        if (validador.validar(expressao)) {
            System.out.println("  Expressao sintaticamente VALIDA.");
        } else {
            System.out.println("  Expressao INVALIDA.");
            System.out.println("  Causa: " + validador.getMensagem());
            if (validador.temPosicao()) {
                apontarPosicao(expressao, validador.getPosicao());
            }
        }
    }

    private void relatarErro(String expressao, String mensagem) {
        System.out.println("  ERRO: " + mensagem);
        if (validador.temPosicao() && expressao != null
                && mensagem != null && mensagem.equals(validador.getMensagem())) {
            apontarPosicao(expressao, validador.getPosicao());
        }
    }

    private void apontarPosicao(String expressao, int posicao) {
        if (expressao == null || posicao < 1 || posicao > expressao.length()) {
            return;
        }
        System.out.println();
        System.out.println("    " + expressao);
        StringBuilder seta = new StringBuilder("    ");
        for (int i = 1; i < posicao; i++) {
            seta.append(' ');
        }
        seta.append('^');
        System.out.println(seta.toString());
        System.out.println("    posicao " + posicao);
    }

    private String comEspacos(String expressao) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (c == ' ' || c == '\t') {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private void ajuda() {
        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("  COMO USAR");
        System.out.println("-----------------------------------------------------------");
        System.out.println("  Digite expressoes infixas usando:");
        System.out.println();
        System.out.println("    operandos   algarismos de 0 a 9 (um unico algarismo)");
        System.out.println("    operadores  +  adicao");
        System.out.println("                -  subtracao");
        System.out.println("                *  multiplicacao");
        System.out.println("                /  divisao");
        System.out.println("                ^  exponenciacao");
        System.out.println("    parenteses  ( )  para alterar a ordem de avaliacao");
        System.out.println();
        System.out.println("  Espacos em branco sao ignorados.");
        System.out.println();
        System.out.println("  Ordem de precedencia, da maior para a menor:");
        System.out.println("    1o)  ^            (associativo a direita)");
        System.out.println("    2o)  *  e  /      (associativos a esquerda)");
        System.out.println("    3o)  +  e  -      (associativos a esquerda)");
        System.out.println();
        System.out.println("  Exemplos validos:");
        System.out.println("    2+3*4              resultado 14");
        System.out.println("    (2+3)*4            resultado 20");
        System.out.println("    2^3^2              resultado 512");
        System.out.println("    (2+5*8)/(4+3)      resultado 6");
        System.out.println();
        System.out.println("  Exemplos invalidos:");
        System.out.println("    (3(+5))            parenteses em posicao indevida");
        System.out.println("    1++2               dois operadores seguidos");
        System.out.println("    12                 dois operandos sem operador");
        System.out.println("    (1+2               parenteses em aberto");
        System.out.println();
        System.out.println("  Observacao: as cinco operacoes sao binarias, portanto");
        System.out.println("  nao e aceito sinal negativo isolado como em -3 ou 2*-3.");
        System.out.println("  Para obter um valor negativo, escreva (0-3).");
        System.out.println("-----------------------------------------------------------");
    }

    private String lerLinha(String rotulo) {
        System.out.print(rotulo);
        System.out.flush();
        try {
            String linha = entrada.readLine();
            return (linha == null) ? "" : linha;
        } catch (IOException e) {
            System.out.println();
            System.out.println("Falha ao ler a entrada: " + e.getMessage());
            return "";
        }
    }

    private boolean executarTestes() {
        totalTestes = 0;
        testesAprovados = 0;

        System.out.println();
        System.out.println("===========================================================");
        System.out.println("  BATERIA DE TESTES AUTOMATIZADOS");
        System.out.println("===========================================================");

        tituloTeste("Conversao infixa -> pos-fixa (tabela 1 do enunciado)");
        conversao("1+2", "1 2 +", "A+B  ->  AB+");
        conversao("1+2-3", "1 2 + 3 -", "A+B-C  ->  AB+C-");
        conversao("(1+2)*(3-4)", "1 2 + 3 4 - *", "(A+B)*(C-D)  ->  AB+CD-*");
        conversao("1^2*3-4+5/6/(7+8)", "1 2 ^ 3 * 4 - 5 6 / 7 8 + / +",
                "A^B*C-D+E/F/(G+H)  ->  AB^C*D-EF/GH+/+");
        conversao("((1+2)*3-(4-5))^(6+7)", "1 2 + 3 * 4 5 - - 6 7 + ^",
                "((A+B)*C-(D-E))^(F+G)  ->  AB+C*DE--FG+^");
        conversao("1-2/(3*4^5)", "1 2 3 4 5 ^ * / -", "A-B/(C*D^E)  ->  ABCDE^*/-");

        tituloTeste("Conversao infixa -> pos-fixa (tabela 2 do enunciado)");
        conversao("1+(2*3)", "1 2 3 * +", "A+(B*C)  ->  ABC*+");
        conversao("(1+2)*3", "1 2 + 3 *", "(A+B)*C  ->  AB+C*");
        conversao("1+2*3", "1 2 3 * +", "A+B*C equivale a A+(B*C)");

        tituloTeste("Precedencia e associatividade");
        conversao("1+2+3", "1 2 + 3 +", "A+B+C equivale a (A+B)+C  [esquerda]");
        conversao("2^3^2", "2 3 2 ^ ^", "A^B^C equivale a A^(B^C)  [direita]");
        conversao("8-3-2", "8 3 - 2 -", "subtracao associa a esquerda");
        conversao("8/4/2", "8 4 / 2 /", "divisao associa a esquerda");
        conversao("2*3^2", "2 3 2 ^ *", "exponenciacao antes da multiplicacao");
        valor("2^3^2", 512.0, "2^(3^2) = 2^9 = 512, e nao (2^3)^2 = 64");
        valor("8-3-2", 3.0, "(8-3)-2 = 3, e nao 8-(3-2) = 7");
        valor("8/4/2", 1.0, "(8/4)/2 = 1, e nao 8/(4/2) = 4");

        tituloTeste("Avaliacao numerica");
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

        tituloTeste("Deteccao de expressoes invalidas");
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

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("  RESULTADO: " + testesAprovados + " de " + totalTestes
                + " casos aprovados.");
        if (testesAprovados == totalTestes) {
            System.out.println("  TODOS OS TESTES PASSARAM.");
        } else {
            System.out.println("  ATENCAO: " + (totalTestes - testesAprovados)
                    + " caso(s) falharam.");
        }
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        return testesAprovados == totalTestes;
    }

    private void tituloTeste(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " ---");
    }

    private void conversao(String expressao, String esperado, String descricao) {
        totalTestes++;
        try {
            if (!validador.validar(expressao)) {
                System.out.println("  [FALHA] " + preencher(expressao)
                        + " -> recusada: " + validador.getMensagem());
            } else {
                String obtido = conversor.converterParaTexto(expressao);
                if (obtido.equals(esperado)) {
                    testesAprovados++;
                    System.out.println("  [OK]    " + preencher(expressao) + " -> " + obtido);
                } else {
                    System.out.println("  [FALHA] " + preencher(expressao) + " -> " + obtido
                            + "   (esperado: " + esperado + ")");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("  [FALHA] " + preencher(expressao)
                    + " -> erro: " + e.getMessage());
        }
        System.out.println("          " + descricao);
    }

    private void valor(String expressao, double esperado, String descricao) {
        totalTestes++;
        try {
            double obtido = avaliarCompleto(expressao);
            if (Math.abs(obtido - esperado) < 1e-9) {
                testesAprovados++;
                System.out.println("  [OK]    " + preencher(expressao) + " = "
                        + AvaliadorPosFixa.formatar(obtido));
            } else {
                System.out.println("  [FALHA] " + preencher(expressao) + " = "
                        + AvaliadorPosFixa.formatar(obtido)
                        + "   (esperado: " + AvaliadorPosFixa.formatar(esperado) + ")");
            }
        } catch (RuntimeException e) {
            System.out.println("  [FALHA] " + preencher(expressao)
                    + " -> recusada: " + e.getMessage());
        }
        System.out.println("          " + descricao);
    }

    private void invalida(String expressao, String descricao) {
        totalTestes++;
        String rotulo = expressao.isEmpty() ? "(vazia)" : expressao;
        try {
            double obtido = avaliarCompleto(expressao);
            System.out.println("  [FALHA] " + preencher(rotulo) + " foi aceita e resultou em "
                    + AvaliadorPosFixa.formatar(obtido));
        } catch (RuntimeException e) {
            testesAprovados++;
            String onde = validador.temPosicao() ? " (posicao " + validador.getPosicao() + ")" : "";
            System.out.println("  [OK]    " + preencher(rotulo) + " recusada" + onde);
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
