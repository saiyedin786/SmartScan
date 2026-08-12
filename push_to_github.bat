@echo off
echo =======================================================
echo     SmartScan - Upload to GitHub for Cloud APK Build
echo =======================================================
echo.

cd /d "%~dp0"

echo Initializing Git repository...
git init
git add .
git commit -m "Build SmartScan Android Document Scanner App"

echo.
set /p REPO_URL="Enter your GitHub Repository URL (e.g. https://github.com/yourusername/SmartScan.git): "

if "%REPO_URL%"=="" (
    echo [!] Repository URL cannot be empty.
    pause
    exit /b
)

git branch -M main
git remote add origin %REPO_URL%
git push -u origin main

echo.
echo =======================================================
echo  Uploaded successfully! 
echo  Go to your GitHub repo -> Actions tab to download APK!
echo =======================================================
pause
