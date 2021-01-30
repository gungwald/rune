@echo off
setlocal
set JAVA_VERSION=1.4
set APPLICATION_JAR=rune.jar
javac -d classes -sourcepath src ^
    -source %JAVA_VERSION% -target %JAVA_VERSION% ^
    src\com\alteredmechanism\notepad\*.java ^
    src\com\alteredmechanism\java\awt\*.java ^
    src\com\alteredmechanism\javax\swing\*.java || exit /b %ERRORLEVEL%
jar -cvf lib\%APPLICATION_JAR% -C classes . || exit /b %ERRORLEVEL%
jar -uvf lib\%APPLICATION_JAR% -C resources . || exit /b %ERRORLEVEL%
rem Still need to create correct manifest for runnable jar
endlocal

