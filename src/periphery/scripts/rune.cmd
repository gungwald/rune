rem Starts Rune on OS/2

set clazzpath=..\lib\jlfgr-1_0.jar;..\lib\rune.jar
set mainclass=com.alteredmechanism.rune.Rune

if not "%JAVA_HOME%"=="" set javaCmd=%JAVA_HOME%\bin\javaw
if     "%JAVA_HOME%"=="" set javaCmd=javaw

rem These are other options to think about.
rem D:\opt\openjdk6_b27
rem set PATH=%JAVA_HOME%\bin;%PATH%

rem OS/2 apparently doesn't support the %* variable. Does it support the 
rem "start" command?

"%javaCmd%" -cp "%clazzpath%" %mainclass% %1 %2 %3 %4 %5 %6 %8 %9

