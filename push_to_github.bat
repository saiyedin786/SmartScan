@echo off
echo =======================================================
echo     SmartScan - Upload Buildscript Repositories Fix
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Add buildscript repositories for AGP and set PREFER_SETTINGS mode"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
