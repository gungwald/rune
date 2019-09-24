@echo off

setlocal
title %~nx0
set PROGRAM_NAME=Notepad

echo.
echo.
echo.
echo.
echo.
echo.
echo.
echo.
echo        %PROGRAM_NAME% Startup Instructions
echo.
echo        Please run %~n0.js instead of %~nx0 because it is able
echo        to start the program without showing a Command Prompt window.
echo.
echo        %PROGRAM_NAME% will now be started after you press a key.
echo.
echo.
echo.
echo.
echo.
echo.
echo.
echo.
echo.
pause

%~dp0notepad.js

endlocal
