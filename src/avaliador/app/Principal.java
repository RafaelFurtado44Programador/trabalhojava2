package avaliador.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import avaliador.nucleo.AvaliadorExpressoes;
import avaliador.nucleo.ExpressaoInvalidaException;
import avaliador.nucleo.ResultadoValidacao;

public class Principal {
    private final AvaliadorExpressoes avaliador = new AvaliadorExpressoes();
    private final BufferedReader entrada =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        Principal programa = new Principal();

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

    private void executar() {
        cabecalho();

        boolean continuar = true;
        while (continuar) {
            menu();
            String opcao = lerLinha("Opcao: ").trim();

            switch (opcao) {
                case "1":
                    opcaoAvaliar();
                    break;
                case "2":
                    opcaoConverter();
                    break;
                case "3":
                    opcaoValidar();
                    break;
                case "4":
                    new TestesAutomatizados().executar();
                    break;
                case "5":
                    ajuda();
                    break;
                case "0":
                    continuar = false;
                    System.out.println();
                    System.out.println("Encerrando. Ate logo!");
                    break;
                default:
                    System.out.println();
                    System.out.println("Opcao invalida. Escolha um numero de 0 a 5.");
                    break;
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
        String expressao = lerLinha("Expressao infixa: ");
        processarExpressao(expressao);
    }

    private void processarExpressao(String expressao) {
        System.out.println();
        try {
            double resultado = avaliador.avaliar(expressao);

            System.out.println("  Forma infixa  : " + avaliador.getUltimaInfixa());
            System.out.println("  Forma pos-fixa: " + avaliador.getUltimaPosFixa());
            System.out.println("  Resultado     : " + AvaliadorExpressoes.formatar(resultado));

        } catch (ExpressaoInvalidaException e) {
            relatarErro(expressao, e);
        }
    }

    private void opcaoConverter() {
        System.out.println();
        String expressao = lerLinha("Expressao infixa: ");
        System.out.println();
        try {
            System.out.println("  Forma pos-fixa: " + avaliador.converterParaPosFixa(expressao));
        } catch (ExpressaoInvalidaException e) {
            relatarErro(expressao, e);
        }
    }

    private void opcaoValidar() {
        System.out.println();
        String expressao = lerLinha("Expressao infixa: ");
        System.out.println();

        ResultadoValidacao resultado = avaliador.validar(expressao);
        if (resultado.ehValida()) {
            System.out.println("  Expressao sintaticamente VALIDA.");
        } else {
            System.out.println("  Expressao INVALIDA.");
            System.out.println("  Causa: " + resultado.getMensagem());
            if (resultado.temPosicao()) {
                apontarPosicao(expressao, resultado.getPosicao());
            }
        }
    }

    private void relatarErro(String expressao, ExpressaoInvalidaException e) {
        System.out.println("  ERRO: " + e.getMessage());
        if (e.temPosicao() && expressao != null) {
            apontarPosicao(expressao, e.getPosicao());
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
}
