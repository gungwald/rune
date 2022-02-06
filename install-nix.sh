#!/bin/sh

# Dev install under OS/2
# Maybe this should be done with the Maven Ant plugin?

# Steps:
# 1. Build, if necessary
# 2. Unzip dist zip to install dir
# 3. Update PATH, if necessary
# 4. Create StartMenu/Desktop icon

INSTALL_DIR=/usr/local
DIST_ZIP=target/rune-1.0-bin.zip

if [ ! -f "%DIST_ZIP%" ]
then
  .\mvnw package || exit
fi

unzip -o "%DIST_ZIP%" -d target
sudo install target/rune-1.0/bin/rune /usr/local/bin
sudo install target/rune-1.0/lib/rune-1.0.jar /usr/local/lib
sudo install target/rune-1.0/lib/jlfgr-1_0.jar /usr/local/lib
sudo target/rune-1.0/bin/install-gnome-icon
