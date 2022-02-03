set JAVA_HOME=D:\opt\openjdk6_b27
set PATH=%JAVA_HOME%\bin;%PATH%

REM OS/2 apparently doesn't support the %* variable

javaw -cp ..\lib\jlfgr-1_0.jar;..\lib\rune-1.0.jar Rune %1 %2 %3 %4 %5 %6 %8 %9