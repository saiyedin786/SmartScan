@echo off
echo =======================================================
echo     SmartScan - Upload Line Ending Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix Windows CRLF line endings for Linux runner"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
