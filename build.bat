@echo off
javac -d classes -sourcepath src -source 1.4 -target 1.4 src/com/alteredmechanism/notepad/*.java src/com/alteredmechanism/java/awt/*.java src/com/alteredmechanism/javax/swing/*.java || exit /b %ERRORLEVEL%
jar -cvf hreodwrit.jar -C classes . || exit /b %ERRORLEVEL%
jar -uvf hreodwrit.jar -C resources . || exit /b %ERRORLEVEL%
rem Still need to create correct manifest

