package avaliador.nucleo;

import avaliador.estruturas.Fila;

/**
 * Fachada do sistema: reune as quatro etapas do processamento em uma unica
 * operacao e guarda os resultados intermediarios para que a camada de
 * apresentacao possa exibi-los.
 *
 * Fluxo completo:
 *
 *   cadeia de caracteres
 *        |  AnalisadorLexico
 *        v
 *   fila de tokens (infixa)
 *        |  ValidadorExpressao
 *        v
 *   fila validada
 *        |  ConversorPosFixa
 *        v
 *   fila de tokens (pos-fixa)
 *        |  AvaliadorPosFixa
 *        v
 *   valor numerico
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public class AvaliadorExpressoes {

    private final AnalisadorLexico analisador;
    private final ValidadorExpressao validador;
    private final ConversorPosFixa conversor;
    private final AvaliadorPosFixa avaliador;

    private String ultimaExpressao;
    private Fila<Token> ultimaInfixa;
    private Fila<Token> ultimaPosFixa;
    private double ultimoResultado;

    public AvaliadorExpressoes() {
        this.analisador = new AnalisadorLexico();
        this.validador = new ValidadorExpressao();
        this.conversor = new ConversorPosFixa();
        this.avaliador = new AvaliadorPosFixa();
    }

    /**
     * Executa o fluxo completo sobre a expressao informada.
     *
     * @param expressao expressao infixa digitada pelo usuario
     * @return valor numerico da expressao
     * @throws ExpressaoInvalidaException se a expressao for rejeitada em
     *                                    qualquer das etapas
     */
    public double avaliar(String expressao) {
        ultimaExpressao = expressao;
        ultimaInfixa = null;
        ultimaPosFixa = null;
        ultimoResultado = 0.0;

        Fila<Token> tokens = analisador.analisar(expressao);
        ultimaInfixa = tokens;

        ResultadoValidacao resultado = validador.validar(tokens);
        if (!resultado.ehValida()) {
            throw new ExpressaoInvalidaException(resultado.getMensagem(),
                    resultado.getPosicao());
        }

        ultimaPosFixa = conversor.converter(tokens);
        ultimoResultado = avaliador.avaliar(ultimaPosFixa);
        return ultimoResultado;
    }

    /**
     * Converte a expressao para a forma pos-fixa, sem calcular o seu valor.
     *
     * @param expressao expressao infixa
     * @return representacao textual da forma pos-fixa
     * @throws ExpressaoInvalidaException se a expressao for rejeitada
     */
    public String converterParaPosFixa(String expressao) {
        Fila<Token> tokens = analisador.analisar(expressao);

        ResultadoValidacao resultado = validador.validar(tokens);
        if (!resultado.ehValida()) {
            throw new ExpressaoInvalidaException(resultado.getMensagem(),
                    resultado.getPosicao());
        }

        return conversor.converter(tokens).toString();
    }

    /**
     * Apenas verifica a validade sintatica, sem converter nem avaliar.
     *
     * @param expressao expressao infixa
     * @return resultado da validacao
     */
    public ResultadoValidacao validar(String expressao) {
        try {
            return validador.validar(analisador.analisar(expressao));
        } catch (ExpressaoInvalidaException e) {
            return ResultadoValidacao.invalida(e.getMessage(), e.getPosicao());
        }
    }

    public String getUltimaExpressao() {
        return ultimaExpressao;
    }

    public String getUltimaInfixa() {
        return (ultimaInfixa == null) ? "" : ultimaInfixa.toString();
    }

    public String getUltimaPosFixa() {
        return (ultimaPosFixa == null) ? "" : ultimaPosFixa.toString();
    }

    public double getUltimoResultado() {
        return ultimoResultado;
    }

    /**
     * Formata um valor numerico para exibicao: sem casas decimais quando o
     * resultado e inteiro, com ate seis casas caso contrario.
     *
     * @param valor valor a formatar
     * @return texto pronto para exibicao
     */
    public static String formatar(double valor) {
        if (Double.isNaN(valor)) {
            return "indefinido";
        }
        if (Double.isInfinite(valor)) {
            return (valor > 0) ? "+infinito" : "-infinito";
        }
        if (Math.abs(valor) >= 1e15) {
            return Double.toString(valor);
        }

        double arredondado = Math.round(valor * 1000000.0) / 1000000.0;
        if (arredondado == Math.floor(arredondado)) {
            return String.valueOf((long) arredondado);
        }
        return Double.toString(arredondado);
    }
}
