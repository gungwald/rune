@echo off

setlocal
title %~nx0

rem If this batch file is chosen, then assume that the user has run it from
rem within a Command Prompt, or that a Command Prompt is desired. So, run
rem the JavaScript startup script with cscript instead of wscript. CScript
rem will write stdout/stderr to the Command Prompt window. WScript will not.
rem Running the JavaScript file directly will bypass the Command Prompt.
cscript //nologo "%~dpn0.js" %*

rem This determines whether it has been double-clicked as an icon in Windows
rem so that it is known that a prompt is needed to stop the Command Prompt
rem window from closing before the user can see what it says. There might be
rem an error that the user needs to see. There might be a final message that
rem is important. And it generally is frustrating to users when windows 
rem disappear before they can read the text.
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


rem A simpler option...

rem @echo off
rem if defined JAVA_HOME (set javaCmd=%JAVA_HOME%\bin\javaw) else javaCmd=javaw
rem start "Rune" "%javaCmd%" -jar %~dp0..\lib\rune~exec.jar

