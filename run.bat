@echo off
setlocal enabledelayedexpansion

cd /d "C:\EMPLOYEE MANAGEMENT SYSTEM"

echo Starting Employee Management System web app...
java -cp "out;lib\*;." employee.management.system.WebServer

pause
