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
| RNF05 | O processamento deve ser linear no tamanho da expressão. |

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

O programa é formado por **sete classes**, todas no diretório `src`, sem uso de
pacotes. Três delas são as estruturas de dados genéricas e três são as etapas do
processamento; a sétima é o ponto de entrada.

```
src/
│
├── No.java                 nó de encadeamento (célula)
├── Pilha.java              pilha LIFO encadeada
├── Fila.java               fila FIFO encadeada
│
├── ValidadorPosFixa.java   etapa 1: validação sintática da expressão infixa
├── ConversorPosFixa.java   etapa 2: conversão infixa -> pós-fixa
├── AvaliadorPosFixa.java   etapa 3: cálculo do valor da forma pós-fixa
│
└── Main.java               menu de console, orquestração e bateria de testes
```

A expressão trafega entre as etapas como **cadeia de caracteres** na entrada e
como **`Fila<Character>`** depois da conversão. Não há classe de token: cada
símbolo é o próprio `char`, o que mantém o programa enxuto e dispensa classes
auxiliares.

### 3.1 Fluxo de processamento

```
   cadeia de caracteres  "(2+5*8)/(4+3)"
            │
            │  ValidadorPosFixa.validar()        ← usa Pilha<Integer>
            ▼
   true  (ou false + mensagem + posição do erro)
            │
            │  ConversorPosFixa.converter()      ← usa Pilha<Character>
            ▼
   Fila<Character> na ordem pós-fixa    2 5 8 * + 4 3 + /
            │
            │  AvaliadorPosFixa.avaliar()        ← usa Pilha<Double>
            ▼
   valor numérico  6
```

`Main` é quem encadeia as três etapas, no método `avaliarCompleto`.

### 3.2 Responsabilidade de cada classe

| Classe | Responsabilidade |
|--------|------------------|
| `No<T>` | Armazenar um valor e a referência ao próximo nó. É a célula usada pela pilha e pela fila. |
| `Pilha<T>` | Disciplina LIFO: `empilhar`, `desempilhar`, `espiar`, `estaVazia`, `tamanho`, `limpar`. |
| `Fila<T>` | Disciplina FIFO: `enfileirar`, `desenfileirar`, `primeiro`, `estaVazia`, `tamanho`, `limpar`, `copiar`. |
| `ValidadorPosFixa` | Varre a expressão infixa, rejeita caracteres fora do alfabeto e verifica a boa formação da sequência. Guarda a mensagem e a posição do erro. Expõe também os testes de classificação `ehOperando` e `ehOperador`, usados pelas outras classes. |
| `ConversorPosFixa` | Aplica o algoritmo do pátio de manobras. Concentra as regras de precedência (`precedencia`) e de associatividade (`associaDireita`). |
| `AvaliadorPosFixa` | Calcula o valor da forma pós-fixa. Concentra a aritmética de cada operador (`aplicar`) e a formatação do resultado (`formatar`). |
| `Main` | Menu de console, orquestração das três etapas, apresentação dos erros e bateria de 43 testes. |

---

## 4. Estruturas de dados implementadas

Nenhuma estrutura pronta da biblioteca Java foi utilizada. As duas estruturas
abaixo foram construídas do zero, por encadeamento simples sobre a célula `No`,
e são genéricas (`<T>`), de modo que servem para guardar `Character`, `Integer`
e `Double` conforme a etapa.

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

**Onde é usada:**

| Etapa | Tipo | Para quê |
|-------|------|----------|
| `ValidadorPosFixa` | `Pilha<Integer>` | guardar a posição de cada `(` em aberto |
| `ConversorPosFixa` | `Pilha<Character>` | guardar operadores e parênteses à espera |
| `AvaliadorPosFixa` | `Pilha<Double>` | guardar os valores parciais do cálculo |

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

**Onde é usada:** como saída do conversor e entrada do avaliador, na forma
`Fila<Character>`.

O método `copiar()` existe para que uma fila possa ser percorrida sem ser
consumida — é o que permite exibir a forma pós-fixa junto com o resultado, já
que a avaliação consome a fila que recebe.

---

## 5. Algoritmos

### 5.1 Validação sintática — autômato de dois estados

`ValidadorPosFixa` percorre a expressão uma única vez, mantendo uma variável
booleana `esperandoOperando` que indica o que pode legitimamente aparecer em
seguida:

| Estado | O que é aceito |
|--------|----------------|
| `esperandoOperando = true` | um algarismo ou `(` |
| `esperandoOperando = false` | um operador ou `)` |

**Transições:**

```
          algarismo                    operador
   ┌──────────────────────┐    ┌──────────────────────┐
   │                      ▼    │                      ▼
 esperando            esperando
 OPERANDO             OPERADOR
   ▲                      │    ▲                      │
   └──────────────────────┘    └──────────────────────┘
            '('                         ')'
```

- `(` mantém o estado em `esperandoOperando` e **empilha** a sua posição.
- `)` leva a `esperandoOperador` e **desempilha** o parêntese correspondente;
  se a pilha estiver vazia, há um fecha sem abre.

**Condição de aceitação:** ao final da varredura, `esperandoOperando` precisa ser
`false` (a expressão terminou com um operando ou um `)`) **e** a pilha de
parênteses precisa estar vazia.

Esse mecanismo único detecta de uma só vez os três problemas exigidos pelo
enunciado:

| Problema | Como é detectado |
|----------|------------------|
| Operandos em excesso ou em falta | estado incompatível ao ler um operando, ou término com `esperandoOperando = true` |
| Operadores em excesso ou em falta | estado incompatível ao ler um operador |
| Parênteses desbalanceados | pilha vazia ao fechar, ou pilha não vazia ao término |
| Posicionamento incorreto | é exatamente o que o estado verifica, a cada símbolo |

Guardar a **posição** de cada `(` na pilha, em vez de guardar apenas uma
contagem, é o que permite apontar exatamente qual parêntese ficou em aberto.

O exemplo `( 3 ( + 5 ) )` citado no enunciado é rejeitado na **posição 3**:
depois de ler o algarismo `3` o autômato espera um operador, e um
abre-parênteses não é aceito nesse estado — falta um operador antes dele.

### 5.2 Conversão infixa → pós-fixa — pátio de manobras

`ConversorPosFixa` percorre os caracteres da esquerda para a direita, apoiado em
uma pilha de operadores e produzindo uma fila de saída:

| Símbolo lido | Ação |
|-----------|------|
| algarismo | vai direto para a fila de saída |
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

| Passo | Símbolo | Pilha de operadores | Fila de saída |
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

`AvaliadorPosFixa` faz uma varredura única apoiada em uma pilha de valores:

```
para cada símbolo da fila pós-fixa:
    se for algarismo -> empilha o seu valor
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

| Símbolo | Operação | Pilha de valores |
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

O erro de sintaxe não usa exceção: `ValidadorPosFixa.validar` devolve `false` e
guarda a causa em `getMensagem()` e o ponto em `getPosicao()`. Já os erros que
só aparecem durante o cálculo são sinalizados por exceções da própria biblioteca
Java — `ArithmeticException` para divisão por zero e exponenciação indefinida,
`IllegalArgumentException` para expressão malformada e `IllegalStateException`
para acesso a estrutura vazia.

`Main` usa a posição guardada pelo validador para desenhar uma seta sob o ponto
do erro:

```
  ERRO: Abre-parenteses em posicao indevida: era esperado um operador
        ou um fecha-parenteses. Falta um operador antes do parenteses.

    (3(+5))
      ^
    posicao 3
```

### Catálogo de erros detectados

| Situação | Exemplo | Onde |
|----------|---------|------|
| Caractere fora do alfabeto | `1+2$3` | validador |
| Expressão vazia | `` ou `   ` | validador |
| Operando em posição indevida | `12` | validador |
| Operador em posição indevida | `1++2`, `+1` | validador |
| Abre-parênteses em posição indevida | `(3(+5))`, `(1+2)(3+4)` | validador |
| Fecha-parênteses em posição indevida | `()`, `(1+)` | validador |
| Fecha sem abre | `1+2)` | validador |
| Abre sem fecha | `(1+2` | validador |
| Expressão incompleta | `1+` | validador |
| Divisão por zero | `1/0` | avaliador |
| Exponenciação indefinida | `0^(0-1)` | avaliador |

---

## 7. Análise de complexidade

Sendo **n** o número de caracteres da expressão:

| Etapa | Tempo | Espaço |
|-------|-------|--------|
| Validação sintática | O(n) | O(n) no pior caso |
| Conversão para pós-fixa | O(n) | O(n) |
| Avaliação da pós-fixa | O(n) | O(n) |
| **Total** | **O(n)** | **O(n)** |

Cada etapa percorre a expressão uma única vez, e todas as operações de pilha e
de fila custam tempo constante. O pior caso de espaço ocorre em expressões
profundamente aninhadas, como `((((1+2))))`, em que a pilha chega a guardar
O(n) parênteses simultaneamente.

---

## 8. Testes

A opção **4** do menu executa 43 casos de teste automatizados, organizados em
cinco grupos. Os testes ficam no próprio `Main`.

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
