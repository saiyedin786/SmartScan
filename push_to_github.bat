@echo off
echo =======================================================
echo     SmartScan - Upload Final Manifest Icon Fix
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix AndroidManifest app icon resource reference"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
