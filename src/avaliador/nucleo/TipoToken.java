package avaliador.nucleo;

/**
 * Natureza de cada simbolo reconhecido dentro de uma expressao.
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public enum TipoToken {

    /** Um algarismo de 0 a 9. */
    OPERANDO,

    /** Um dos operadores binarios: + - * / ^ */
    OPERADOR,

    /** O simbolo de abertura de parenteses: ( */
    ABRE_PARENTESE,

    /** O simbolo de fechamento de parenteses: ) */
    FECHA_PARENTESE
}
