@echo off
echo =======================================================
echo     SmartScan - Upload Plugin Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix Android Gradle Plugin versions and Proguard config"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
