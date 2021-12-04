@echo off

set TOP_DIR=%~dp0
set BIN_DIR="%TOP_DIR%"src\main\dist\bin

"%BIN_DIR%"\rune.bat "%TOP_DIR%"pom.xml "%TOP_DIR%"edit-sources* ^
	"%TOP_DIR%"test "%TOP_DIR%"test.bat ^
	"%BIN_DIR%"\* ^
	"%TOP_DIR%"src\main\java\com\alteredmechanism\notepad\*.java
