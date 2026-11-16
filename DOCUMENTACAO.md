# Avaliador de Expressões Matemáticas

**Trabalho 2 — Laboratório de Algoritmos e Estruturas de Dados I**
Engenharia da Computação — CEFET-MG, Campus Timóteo
Prof. Luciano Moreira — Valor: 30 pontos

---

## Sumário

1. [Visão geral](#1-visão-geral)
2. [Especificação de requisitos](#2-especificação-de-requisitos)
3. [Arquitetura do programa](#3-arquitetura-do-programa)
4. [Estruturas de dados implementadas](#4-estruturas-de-dados-implementadas)
5. [Algoritmos](#5-algoritmos)
6. [Tratamento de erros](#6-tratamento-de-erros)
7. [Análise de complexidade](#7-análise-de-complexidade)
8. [Testes](#8-testes)
9. [Observação sobre um exemplo do enunciado](#9-observação-sobre-um-exemplo-do-enunciado)

---

## 1. Visão geral

O programa recebe uma expressão aritmética escrita na forma **infixa** (a forma
usual, em que o operador fica entre os dois operandos), verifica se ela é
sintaticamente válida, converte-a para a forma **pós-fixa** (notação polonesa
inversa) e calcula o seu valor numérico.

A conversão para a forma pós-fixa é o passo central do trabalho: uma vez que a
expressão está nessa notação, os parênteses deixam de ser necessários e a
ordem dos operadores já determina sozinha a ordem das operações, o que torna a
avaliação uma varredura simples apoiada em uma pilha.

**Exemplo completo:**

```
Entrada:         (2+5*8)/(4+3)
Forma pós-fixa:  2 5 8 * + 4 3 + /
Resultado:       6
```

---

## 2. Especificação de requisitos

### 2.1 Requisitos funcionais

| ID | Requisito |
|----|-----------|
| RF01 | Ler uma expressão aritmética infixa fornecida como cadeia de caracteres. |
| RF02 | Aceitar como operandos apenas algarismos isolados de `0` a `9`. |
| RF03 | Aceitar as cinco operações binárias: adição `+`, subtração `-`, multiplicação `*`, divisão `/` e exponenciação `^`. |
| RF04 | Aceitar parênteses `(` e `)` como mecanismo de agrupamento. |
| RF05 | Ignorar espaços em branco na entrada. |
| RF06 | Verificar, **antes de avaliar**, se a expressão é sintaticamente válida. |
| RF07 | Detectar número incorreto de operandos, de operadores e de parênteses. |
| RF08 | Detectar posicionamento incorreto de qualquer um desses elementos dentro da expressão. |
| RF09 | Informar a causa do erro e a posição exata em que foi detectado. |
| RF10 | Determinar automaticamente a ordem de precedência das operações, sem exigir parênteses redundantes. |
| RF11 | Converter a expressão da forma infixa para a forma pós-fixa. |
| RF12 | Exibir a forma pós-fixa obtida. |
| RF13 | Calcular e apresentar o valor numérico da expressão. |
| RF14 | Detectar divisão por zero durante a avaliação. |
| RF15 | Oferecer uma bateria de testes automatizados que reproduza os exemplos do enunciado. |

### 2.2 Requisitos não funcionais

| ID | Requisito |
|----|-----------|
| RNF01 | O programa deve ser escrito integralmente em Java. |
| RNF02 | **Não é permitido o uso de estruturas de dados prontas da biblioteca Java** (`Stack`, `ArrayList`, `LinkedList` ou similares). Todas as estruturas e funções utilizadas foram implementadas neste trabalho. |
| RNF03 | As estruturas devem ser genéricas e reaproveitáveis. |
| RNF04 | Cada etapa do processamento deve residir em uma classe própria, com responsabilidade única. |
| RNF05 | O código deve ser documentado e legível. |
| RNF06 | O processamento deve ser linear no tamanho da expressão. |

### 2.3 Regras de precedência e associatividade

Da precedência mais alta para a mais baixa:

| Nível | Operadores | Associatividade |
|-------|-----------|-----------------|
| 3 | `^` | **à direita** |
| 2 | `*` `/` | à esquerda |
| 1 | `+` `-` | à esquerda |

A associatividade decide o agrupamento quando operadores de mesma precedência
aparecem em sequência, exatamente como descrito no enunciado:

```
A + B + C   equivale a   (A + B) + C      associatividade à esquerda
A ^ B ^ C   equivale a   A ^ (B ^ C)      associatividade à direita
```

### 2.4 Restrições conhecidas

- Operandos são algarismos isolados; `12` é lido como dois operandos
  consecutivos e, portanto, rejeitado.
- As cinco operações são **binárias**, conforme o enunciado. Não há operador
  unário, de modo que `-3` ou `2*-3` são expressões inválidas. Para obter um
  valor negativo escreva `(0-3)`.
- Os cálculos usam ponto flutuante de dupla precisão (`double`).

---

## 3. Arquitetura do programa

O programa está dividido em três pacotes, segundo o princípio de separação de
responsabilidades.

```
avaliador
│
├── estruturas        estruturas de dados genéricas, de uso geral
│   ├── No                      nó de encadeamento
│   ├── Pilha                   pilha LIFO encadeada
│   ├── Fila                    fila FIFO encadeada
│   └── EstruturaVaziaException
│
├── nucleo            regra de negócio: as quatro etapas do processamento
│   ├── TipoToken               enumeração: operando, operador, parênteses
│   ├── Associatividade         enumeração: esquerda, direita
│   ├── Operador                catálogo dos cinco operadores + regra de cálculo
│   ├── Token                   unidade léxica + posição na expressão
│   ├── AnalisadorLexico        etapa 1: texto  -> fila de tokens
│   ├── ValidadorExpressao      etapa 2: validação sintática
│   ├── ResultadoValidacao      resposta do validador
│   ├── ConversorPosFixa        etapa 3: infixa -> pós-fixa
│   ├── AvaliadorPosFixa        etapa 4: pós-fixa -> valor numérico
│   ├── AvaliadorExpressoes     fachada que encadeia as quatro etapas
│   └── ExpressaoInvalidaException
│
└── app               camada de apresentação
    ├── Principal               menu de console / linha de comando
    └── TestesAutomatizados     bateria de testes de aceitação
```

### 3.1 Fluxo de processamento

```
   cadeia de caracteres  "(2+5*8)/(4+3)"
            │
            │  AnalisadorLexico.analisar()
            ▼
   Fila<Token> na ordem infixa
            │
            │  ValidadorExpressao.validar()
            ▼
   ResultadoValidacao  (válida / inválida + posição)
            │
            │  ConversorPosFixa.converter()        ← usa Pilha<Token>
            ▼
   Fila<Token> na ordem pós-fixa
            │
            │  AvaliadorPosFixa.avaliar()          ← usa Pilha<Double>
            ▼
   valor numérico  6
```

A classe `AvaliadorExpressoes` é a fachada: ela encadeia as quatro etapas e
guarda os resultados intermediários, de modo que a camada de apresentação
possa exibir a forma pós-fixa junto com o resultado sem conhecer os detalhes
internos.

### 3.2 Responsabilidade de cada classe

| Classe | Responsabilidade |
|--------|------------------|
| `No` | Armazenar um valor e a referência ao próximo nó. |
| `Pilha` | Operações `empilhar`, `desempilhar`, `espiar`, `estaVazia`, `tamanho`. |
| `Fila` | Operações `enfileirar`, `desenfileirar`, `primeiro`, `estaVazia`, `tamanho`, `copiar`. |
| `TipoToken` | Classificar o símbolo em operando, operador ou parêntese. |
| `Associatividade` | Representar o sentido de associação de um operador. |
| `Operador` | Guardar símbolo, precedência, associatividade e a regra de cálculo de cada operação. |
| `Token` | Representar um símbolo reconhecido junto com a sua posição no texto original. |
| `AnalisadorLexico` | Quebrar o texto em tokens e rejeitar caracteres fora do alfabeto. |
| `ValidadorExpressao` | Verificar a boa formação da sequência de tokens. |
| `ResultadoValidacao` | Transportar o veredito do validador, com mensagem e posição. |
| `ConversorPosFixa` | Aplicar o algoritmo do pátio de manobras. |
| `AvaliadorPosFixa` | Calcular o valor da expressão pós-fixa. |
| `AvaliadorExpressoes` | Encadear as etapas e expor uma interface única. |
| `Principal` | Interação com o usuário pelo console. |
| `TestesAutomatizados` | Executar e relatar os casos de teste. |

---

## 4. Estruturas de dados implementadas

Nenhuma estrutura pronta da biblioteca Java foi utilizada. As duas estruturas
abaixo foram construídas do zero, por encadeamento simples, e são genéricas
(`<T>`), de modo que servem tanto para guardar `Token` quanto `Double`.

### 4.1 Pilha — `Pilha<T>`

Disciplina LIFO. O topo é sempre o primeiro nó da cadeia, o que dá custo
constante a todas as operações.

```
topo ──► [ * ] ──► [ + ] ──► [ ( ] ──► null
```

| Operação | Custo |
|----------|-------|
| `empilhar(T)` | O(1) |
| `desempilhar()` | O(1) |
| `espiar()` | O(1) |
| `estaVazia()` | O(1) |
| `tamanho()` | O(1) |

**Onde é usada:** na pilha de operadores do conversor, na pilha de parênteses
do validador e na pilha de valores do avaliador.

### 4.2 Fila — `Fila<T>`

Disciplina FIFO. Mantém referências para o início e para o fim, o que também
garante custo constante.

```
inicio ──► [ 2 ] ──► [ 5 ] ──► [ 8 ] ──► null
                                  ▲
                                 fim
```

| Operação | Custo |
|----------|-------|
| `enfileirar(T)` | O(1) |
| `desenfileirar()` | O(1) |
| `primeiro()` | O(1) |
| `estaVazia()` | O(1) |
| `tamanho()` | O(1) |
| `copiar()` | O(n) |

**Onde é usada:** para transportar a sequência de tokens entre as etapas, tanto
na ordem infixa quanto na pós-fixa.

O método `copiar()` existe para que uma fila possa ser percorrida sem ser
consumida — é o que permite exibir a forma infixa e a pós-fixa depois de a
expressão já ter sido avaliada.

---

## 5. Algoritmos

### 5.1 Validação sintática — autômato de dois estados

O validador percorre a fila de tokens uma única vez, mantendo um estado que
indica o que pode legitimamente aparecer em seguida:

| Estado | O que é aceito |
|--------|----------------|
| `ESPERANDO_OPERANDO` | um algarismo ou `(` |
| `ESPERANDO_OPERADOR` | um operador ou `)` |

**Transições:**

```
          algarismo                    operador
   ┌──────────────────────┐    ┌──────────────────────┐
   │                      ▼    │                      ▼
ESPERANDO_OPERANDO     ESPERANDO_OPERADOR
   ▲                      │    ▲                      │
   └──────────────────────┘    └──────────────────────┘
            '('                         ')'
```

- `(` mantém o estado em `ESPERANDO_OPERANDO` e **empilha** o parêntese.
- `)` leva a `ESPERANDO_OPERADOR` e **desempilha** o parêntese correspondente;
  se a pilha estiver vazia, há um fecha sem abre.

**Condição de aceitação:** ao final da varredura, o autômato precisa estar em
`ESPERANDO_OPERADOR` (a expressão terminou com um operando ou um `)`) **e** a
pilha de parênteses precisa estar vazia.

Esse mecanismo único detecta de uma só vez os três problemas exigidos pelo
enunciado:

| Problema | Como é detectado |
|----------|------------------|
| Operandos em excesso ou em falta | estado incompatível ao ler um operando, ou término em `ESPERANDO_OPERANDO` |
| Operadores em excesso ou em falta | estado incompatível ao ler um operador |
| Parênteses desbalanceados | pilha vazia ao fechar, ou pilha não vazia ao término |
| Posicionamento incorreto | é exatamente o que o estado verifica, a cada token |

O exemplo `( 3 ( + 5 ) )` citado no enunciado é rejeitado na **posição 3**:
depois de ler o algarismo `3` o autômato está em `ESPERANDO_OPERADOR`, e um
abre-parênteses não é aceito nesse estado — falta um operador antes dele.

### 5.2 Conversão infixa → pós-fixa — pátio de manobras

Percorre os tokens da esquerda para a direita, apoiado em uma pilha de
operadores e produzindo uma fila de saída:

| Token lido | Ação |
|-----------|------|
| operando | vai direto para a fila de saída |
| operador | enquanto o topo da pilha for um operador que deva sair antes, desempilha para a saída; em seguida empilha o operador lido |
| `(` | empilha, funcionando como marcador de fronteira |
| `)` | desempilha para a saída até encontrar o `(` correspondente, que é descartado |

Ao final, o que restou na pilha é transferido para a saída.

**Critério de desempilhamento** (método `deveDesempilhar`), que é onde a
associatividade entra:

```
se o operador atual é associativo à ESQUERDA:
    desempilha enquanto  precedência(topo) >= precedência(atual)

se o operador atual é associativo à DIREITA:
    desempilha enquanto  precedência(topo) >  precedência(atual)
```

É essa diferença de um único sinal que faz `8-3-2` ser lido como `(8-3)-2 = 3`
e `2^3^2` ser lido como `2^(3^2) = 512`.

**Traçado de `(2+5*8)/(4+3)`:**

| Passo | Token | Pilha de operadores | Fila de saída |
|-------|-------|--------------------|----------------|
| 1 | `(` | `(` | |
| 2 | `2` | `(` | `2` |
| 3 | `+` | `( +` | `2` |
| 4 | `5` | `( +` | `2 5` |
| 5 | `*` | `( + *` | `2 5` |
| 6 | `8` | `( + *` | `2 5 8` |
| 7 | `)` | | `2 5 8 * +` |
| 8 | `/` | `/` | `2 5 8 * +` |
| 9 | `(` | `/ (` | `2 5 8 * +` |
| 10 | `4` | `/ (` | `2 5 8 * + 4` |
| 11 | `+` | `/ ( +` | `2 5 8 * + 4` |
| 12 | `3` | `/ ( +` | `2 5 8 * + 4 3` |
| 13 | `)` | `/` | `2 5 8 * + 4 3 +` |
| — | fim | | `2 5 8 * + 4 3 + /` |

### 5.3 Avaliação da forma pós-fixa

Varredura única apoiada em uma pilha de valores:

```
para cada token da fila pós-fixa:
    se for operando  -> empilha o seu valor
    se for operador  -> desempilha dois valores,
                        aplica a operação,
                        empilha o resultado
```

**Ponto de atenção:** o **primeiro** valor desempilhado é o operando da
**direita** e o **segundo** é o da **esquerda**. Inverter essa ordem quebraria
a subtração, a divisão e a exponenciação.

Ao final, a pilha deve conter exatamente um valor — o resultado. Se contiver
mais de um, sobraram operandos sem operador.

**Traçado de `2 5 8 * + 4 3 + /`:**

| Token | Operação | Pilha de valores |
|-------|----------|------------------|
| `2` | empilha | `2` |
| `5` | empilha | `2 5` |
| `8` | empilha | `2 5 8` |
| `*` | 5 × 8 = 40 | `2 40` |
| `+` | 2 + 40 = 42 | `42` |
| `4` | empilha | `42 4` |
| `3` | empilha | `42 4 3` |
| `+` | 4 + 3 = 7 | `42 7` |
| `/` | 42 ÷ 7 = 6 | `6` |

**Resultado: 6**

---

## 6. Tratamento de erros

Todos os erros são sinalizados por `ExpressaoInvalidaException`, que carrega a
mensagem e, quando aplicável, a posição do caractere problemático. A camada de
apresentação usa essa posição para desenhar uma seta sob o ponto do erro:

```
  ERRO: Abre-parenteses em posicao indevida: era esperado um operador
        ou um fecha-parenteses. Falta um operador antes do parenteses.

    (3(+5))
      ^
    posicao 3
```

### Catálogo de erros detectados

| Situação | Exemplo | Etapa |
|----------|---------|-------|
| Caractere fora do alfabeto | `1+2$3` | léxica |
| Expressão vazia | `` ou `   ` | léxica |
| Operando em posição indevida | `12` | validação |
| Operador em posição indevida | `1++2`, `+1` | validação |
| Abre-parênteses em posição indevida | `(3(+5))`, `(1+2)(3+4)` | validação |
| Fecha-parênteses em posição indevida | `()`, `(1+)` | validação |
| Fecha sem abre | `1+2)` | validação |
| Abre sem fecha | `(1+2` | validação |
| Expressão incompleta | `1+` | validação |
| Divisão por zero | `1/0` | avaliação |
| Exponenciação indefinida | `0^(0-1)` | avaliação |

---

## 7. Análise de complexidade

Sendo **n** o número de caracteres da expressão:

| Etapa | Tempo | Espaço |
|-------|-------|--------|
| Análise léxica | O(n) | O(n) |
| Validação sintática | O(n) | O(n) no pior caso |
| Conversão para pós-fixa | O(n) | O(n) |
| Avaliação da pós-fixa | O(n) | O(n) |
| **Total** | **O(n)** | **O(n)** |

Cada etapa percorre a sequência de tokens uma única vez, e todas as operações
de pilha e de fila custam tempo constante. O pior caso de espaço ocorre em
expressões profundamente aninhadas, como `((((1+2))))`, em que a pilha chega a
guardar O(n) parênteses simultaneamente.

---

## 8. Testes

A opção **4** do menu executa 43 casos de teste automatizados, organizados em
cinco grupos.

### 8.1 Conversão — tabelas do enunciado

Como o programa aceita apenas operandos de um algarismo, as variáveis das
tabelas do enunciado foram substituídas pela sua posição no alfabeto
(`A=1, B=2, …, H=8`). A substituição preserva integralmente a **estrutura** da
conversão, que é o que os casos verificam.

| Enunciado (infixa) | Enunciado (pós-fixa) | Teste executado | Saída obtida |
|--------------------|----------------------|-----------------|--------------|
| `A+B` | `AB+` | `1+2` | `1 2 +` |
| `A+B-C` | `AB+C-` | `1+2-3` | `1 2 + 3 -` |
| `(A+B)*(C-D)` | `AB+CD-*` | `(1+2)*(3-4)` | `1 2 + 3 4 - *` |
| `A^B*C-D+E/F/(G+H)` | `AB^C*D-EF/GH+/+` | `1^2*3-4+5/6/(7+8)` | `1 2 ^ 3 * 4 - 5 6 / 7 8 + / +` |
| `((A+B)*C-(D-E))^(F+G)` | `AB+C*DE--FG+^` | `((1+2)*3-(4-5))^(6+7)` | `1 2 + 3 * 4 5 - - 6 7 + ^` |
| `A-B/(C*D^E)` | `ABCDE^*/-` | `1-2/(3*4^5)` | `1 2 3 4 5 ^ * / -` |
| `A+(B*C)` | `ABC*+` | `1+(2*3)` | `1 2 3 * +` |
| `(A+B)*C` | `AB+C*` | `(1+2)*3` | `1 2 + 3 *` |

As oito conversões conferem com as tabelas do enunciado.

### 8.2 Demais grupos

- **Precedência e associatividade** — verifica que `2^3^2 = 512` (e não 64),
  que `8-3-2 = 3` (e não 7) e que `8/4/2 = 1` (e não 4).
- **Avaliação numérica** — treze casos cobrindo as cinco operações, parênteses
  redundantes, resultado fracionário e base negativa.
- **Expressões inválidas** — treze casos que devem ser recusados, incluindo o
  `(3(+5))` do enunciado.

### 8.3 Resultado

```
RESULTADO: 43 de 43 casos aprovados.
TODOS OS TESTES PASSARAM.
```

---

## 9. Observação sobre um exemplo do enunciado

O enunciado afirma, na página 2:

> "Ao final, o programa deverá apresentar o valor numérico da expressão
> avaliada, por exemplo, para a expressão `2 + 5 * 8 / ( 4 + 3 )` o valor a ser
> apresentado será **6**."

Aplicando as regras de precedência descritas no próprio enunciado, essa
expressão vale:

```
2 + 5 * 8 / (4 + 3)
= 2 + (5 * 8) / 7
= 2 + 40/7
= 2 + 5,714285…
= 7,714285…
```

O valor **6** corresponde a `(2 + 5 * 8) / (4 + 3) = 42 / 7 = 6`, ou seja, à
mesma expressão com um par de parênteses adicional agrupando o numerador.

O programa implementa as regras de precedência tais como o enunciado as
descreve, de modo que:

| Expressão | Resultado |
|-----------|-----------|
| `2+5*8/(4+3)` | `7.714286` |
| `(2+5*8)/(4+3)` | `6` |

Ambos os casos estão cobertos pela bateria de testes.
