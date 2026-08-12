@echo off
echo =======================================================
echo     SmartScan - Upload Kotlin Version Match Fix
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Match Kotlin version 1.9.20 with Compose compiler 1.5.4"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
