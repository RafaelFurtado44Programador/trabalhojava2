@echo off
REM ======================================================================
REM  Avaliador de Expressoes Matematicas
REM  Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
REM  Script de compilacao para Windows
REM ======================================================================

echo.
echo Compilando o projeto...
echo.

if not exist bin mkdir bin

javac -encoding UTF-8 -d bin ^
    src\avaliador\estruturas\*.java ^
    src\avaliador\nucleo\*.java ^
    src\avaliador\app\*.java

if errorlevel 1 (
    echo.
    echo FALHA NA COMPILACAO.
    exit /b 1
)

echo.
echo Compilacao concluida. Os arquivos .class estao na pasta "bin".
echo Para executar, rode:  executar.bat
echo.
