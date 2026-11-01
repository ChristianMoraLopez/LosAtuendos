@echo off
rem Abre Los Atuendos. Usa el Java del PATH o, si no hay, el JDK 18 instalado en D:\Programas.
cd /d "%~dp0"
set "JAVAW=javaw"
where javaw >nul 2>nul || set "JAVAW=D:\Programas\Java\openjdk-18.0.2.1_windows-x64_bin\jdk-18.0.2.1\bin\javaw.exe"
start "" "%JAVAW%" -jar dist\LosAtuendos.jar
