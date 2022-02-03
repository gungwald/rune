@echo off
setlocal
title %~nx0

rem If this batch file is chosen, then assume that the user has run it from
rem within a Command Prompt, or that a Command Prompt is desired. So, run
rem the JavaScript startup script with cscript instead of wscript. CScript
rem will write stdout/stderr to the Command Prompt window. WScript will not.
rem Running the JavaScript file directly will bypass the Command Prompt.
cscript //nologo "%~dpn0.js" %*

for /f "tokens=2" %%a in ("%CMDCMDLINE%") do (
    if "%%a"=="/c" (
        pause
    )
)
title Command Prompt
endlocal

goto :EOF


rem ***********************************************************************
rem Currently not needed but kept for reference in case it is needed later:
rem ***********************************************************************

rem Determine if this script was run from a Command Prompt or from
rem Windows Explorer. Windows Explorer uses the /c switch. This does
rem not happen from the Command Prompt.
set WSH_ENGINE=
for /f "tokens=2" %%a in ("%CMDCMDLINE%") do (
    if "%%a"=="/c" (
        WSH_ENGINE=wscript
    ) else (
        WSH_ENGINE=cscript
    )
)

