@echo off
echo =======================================================
echo     SmartScan - Upload to GitHub for Cloud APK Build
echo =======================================================
echo.

cd /d "%~dp0"

echo Initializing Git repository...
git init
git add .
git commit -m "Add full SmartScan Android codebase and GitHub Actions APK build workflow"

echo.
git remote set-url origin https://github.com/saiyedin786/SmartScan.git 2>nul || git remote add origin https://github.com/saiyedin786/SmartScan.git

git branch -M main
git push -u origin main

echo.
echo =======================================================
echo  Uploaded successfully to https://github.com/saiyedin786/SmartScan !
echo  Go to your GitHub repo -> Actions tab to download your APK!
echo =======================================================
pause
