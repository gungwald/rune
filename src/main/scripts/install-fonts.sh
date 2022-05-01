#!/bin/sh

# Finds all .ttf files below the directory this script is in and
# copies them to the system font directory.

getSystemFontDir()
(
    OS=`uname -s`
    if [ "$OS" = 'darwin' ]; then
        SYSTEM_FONT_DIR='/Library/Fonts'
    else
        echo Unknown system $OS
        exit 1
    fi
)

getAbsolutePath()
(
    SHORT_NAME="$1"
    if [ -d "$SHORT_NAME" ]; then
        ( cd `dirname "$SHORT_NAME"`
          pwd )
    else
        ( cd `dirname "$SHORT_NAME"`
          echo `pwd`/`basename "$SHORT_NAME"` )
    fi
)

installFont()
(
    FONT_FILE="$1"
    INSTALL_DIR="$2"
    sudo cp -ipv "$FONT_FILE" "$INSTALL_DIR"
)

SELF=`getAbsolutePath "$0"`
MY_DIR=`dirname "$SELF"`
SYSTEM_FONT_DIR=`getSystemFontDir`
find "$MY_DIR" -name '*.ttf' -exec installFont '{}' "$SYSTEM_FONT_DIR" ;

