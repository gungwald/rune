REM Dev install under OS/2
REM Maybe this should be done with the Maven Ant plugin?

REM Steps:
REM 1. Build, if necessary
REM 2. Unzip dist zip to install dir
REM 3. Update PATH, if necessary
REM 4. Create StartMenu/Desktop icon

set INSTALL_DIR=D:\opt
set DIST_ZIP=target/rune-1.0-bin.zip

if not exist "%DIST_ZIP%" .\mvnw package
unzip -o "%DIST_ZIP%" -d "%INSTALL_DIR%"
