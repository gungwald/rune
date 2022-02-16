#!/bin/sh

# Dev install under SunOS because it's version of "install" is weird.
# Maybe this should be done with the Maven Ant plugin?

# Steps:
# 1. Build, if necessary
# 2. Unzip dist zip to install dir
# 3. Update PATH, if necessary
# 4. Create StartMenu/Desktop icon
# 5. Install bundled fonts

INSTALL_DIR=/usr/local
DIST_ZIP=target/rune-1.0-bin.zip

if [ ! -f "$DIST_ZIP" ]
then
  ./mvnw package || exit
fi

unzip -o "$DIST_ZIP" -d target
sudo install -d /usr/local
sudo install -d /usr/local/bin
sudo install -d /usr/local/lib
sudo install -f /usr/local/bin target/rune-1.0/bin/rune
sudo install -f /usr/local/lib target/rune-1.0/lib/rune-1.0.jar
sudo install -f /usr/local/lib target/rune-1.0/lib/jlfgr-1_0.jar
sudo target/rune-1.0/bin/install-gnome-icon
