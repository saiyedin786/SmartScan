@echo off
echo =======================================================
echo     SmartScan - Upload Fix to GitHub Actions
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Add gradlew script and setup-gradle workflow for online build"
git push -u origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
