@echo off
echo =======================================================
echo     SmartScan - Upload Plugin ID Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix Kotlin plugin IDs to org.jetbrains.kotlin.android and org.jetbrains.kotlin.kapt"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
