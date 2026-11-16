@echo off
REM ======================================================================
REM  Avaliador de Expressoes Matematicas
REM  Trabalho 2 - Lab. AED I - CEFET-MG Campus Timoteo
REM  Script de execucao para Windows
REM ======================================================================

if not exist bin (
    echo.
    echo O projeto ainda nao foi compilado.
    echo Rode primeiro:  compilar.bat
    echo.
    exit /b 1
)

java -cp bin avaliador.app.Principal %*
