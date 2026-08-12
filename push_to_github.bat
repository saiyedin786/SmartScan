@echo off
echo =======================================================
echo     SmartScan - Upload Wrapper Jar Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add -f gradle/wrapper/gradle-wrapper.jar
git add .
git commit -m "Use setup-gradle action and include wrapper jar"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
