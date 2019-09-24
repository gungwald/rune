@echo off
setlocal
title %~nx0
set PROGRAM_NAME=Notepad

rem Determine if this script was run from a Command Prompt or from
rem Windows Explorer. Windows Explorer uses the /c switch. This does
rem not happen from the Command Prompt.
for /f "tokens=2" %%a in ("%CMDCMDLINE%") do (
    if "%%a"=="/c" (
        rem Started from Windows Explorer
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
    )
)

%~dp0notepad.js %*
title Command Prompt

endlocal
