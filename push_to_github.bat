@echo off
echo =======================================================
echo     SmartScan - Upload Standard Build Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Switch to standard AGP buildscript classpath & gradlew execution"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
