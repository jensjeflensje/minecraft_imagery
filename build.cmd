@echo off
echo Original Java Home: %JAVA_HOME%
set "JAVA_DIR=C:\Program Files\Java\jdk-17"

echo confirming that current jdk is version 17
set "SEARCH_STR=17"
echo %JAVA_HOME% | findstr /c:"%SEARCH_STR%" >nul
if %errorlevel% equ 0 (
    echo JAVA_HOME contains "%SEARCH_STR%"
    goto :exists
)

if exist "%JAVA_DIR%" (
    set "JAVA_HOME=%JAVA_DIR%"
    echo Running with Java Home: %JAVA_HOME%
    goto :exists
) 
echo Directory %JAVA_DIR% does not exist. JAVA_HOME not set.
goto :end
:exists

echo Building ImageryAPI with %JAVA_HOME%

mvn package

:end
echo DONE
