@echo off
echo =======================================================
echo     SmartScan - Upload Repository Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix settings repositories conflict in build.gradle.kts"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
