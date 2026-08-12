@echo off
echo =======================================================
echo     SmartScan - Upload Configuration Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Add gradle.properties and sync Compose compiler version"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
