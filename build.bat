@echo off
javac -d classes -sourcepath src -target 1.4 || exit /b %ERRORLEVEL%
jar -cvf hreodwrit.jar -C classes * || exit /b %ERRORLEVEL%
jar -uvf hreodwrit.jar -C resources * || exit /b %ERRORLEVEL%

