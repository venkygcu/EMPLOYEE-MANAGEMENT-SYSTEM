@echo off
setlocal enabledelayedexpansion

cd /d "C:\EMPLOYEE MANAGEMENT SYSTEM"

if not exist out mkdir out

echo Compiling Java files...
javac -d out -cp "lib\*;." employee\management\system\*.java

if %ERRORLEVEL% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed with errors.
)
pause
