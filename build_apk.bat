@echo off
echo =======================================================
echo         SmartScan - Building Android APK
echo =======================================================
echo.

cd /d "%~dp0"

echo Running Gradle APK build...
call gradlew.bat assembleDebug

echo.
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo =======================================================
    echo   SUCCESS! APK successfully generated at:
    echo   %~dp0app\build\outputs\apk\debug\app-debug.apk
    echo =======================================================
) else (
    echo [!] Build failed or Java JDK is required on system.
)

pause
