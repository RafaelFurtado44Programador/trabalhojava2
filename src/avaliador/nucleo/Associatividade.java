package avaliador.nucleo;

/**
 * Sentido de associacao de um operador quando ele aparece em sequencia com
 * outro de mesma precedencia.
 *
 * Conforme o enunciado do trabalho:
 *   A + B + C  significa  (A + B) + C   -> associatividade a ESQUERDA
 *   A ^ B ^ C  significa  A ^ (B ^ C)   -> associatividade a DIREITA
 *
 * Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
 */
public enum Associatividade {

    ESQUERDA,
    DIREITA
}
