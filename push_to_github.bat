@echo off
echo =======================================================
echo     SmartScan - Upload Gradle 8.4 Version Pin Fix
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Pin Gradle version to 8.4 for AGP 8.1.4 compatibility"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
