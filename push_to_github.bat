@echo off
echo =======================================================
echo     SmartScan - Upload JVM Opts Fix to GitHub
echo =======================================================
echo.

cd /d "%~dp0"

git add .
git commit -m "Fix DEFAULT_JVM_OPTS ClassNotFoundException in gradlew"
git push origin main

echo.
echo =======================================================
echo  Pushed successfully! 
echo  Check https://github.com/saiyedin786/SmartScan/actions
echo =======================================================
pause
