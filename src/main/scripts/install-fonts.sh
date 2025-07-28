#!/bin/sh

# Finds all .ttf files below the directory this script is in and
# copies them to the system font directory.

getSystemFontDir()
(
    # shellcheck disable=SC2006
    OS=`uname -s`
    if [ "$OS" = 'darwin' ]; then
        SYSTEM_FONT_DIR='/Library/Fonts'
    elif [ "$OS" = 'Linux' ]; then
        SYSTEM_FONT_DIR="$HOME/.fonts"
    else
        echo Unknown operating system $OS. Cannot determine font directory. 1>&2
        exit 1
    fi
    echo $SYSTEM_FONT_DIR
)

getAbsolutePath()
(
    SHORT_NAME="$1"
    if [ -d "$SHORT_NAME" ]; then
        # shellcheck disable=SC2006
        ( cd "`dirname "$SHORT_NAME"`" || exit
          pwd )
    else
        # shellcheck disable=SC2006
        ( cd "`dirname "$SHORT_NAME"`" || exit
          echo "`pwd`"/"`basename "$SHORT_NAME"`" )
    fi
)

# shellcheck disable=SC2006
SELF=`getAbsolutePath "$0"`
# shellcheck disable=SC2006
MY_DIR=`dirname "$SELF"`
FONT_SRC_DIR=`dirname "$MY_DIR"`/resources/fonts
FONT_DEST_DIR=`getSystemFontDir`

if [ ! -d "$FONT_DEST_DIR" ]; then
    mkdir -p "$FONT_DEST_DIR"
fi

# Find and install any TrueType fonts.
# shellcheck disable=SC2006
find "$FONT_SRC_DIR" -name '*.ttf' -exec cp -ipv '{}' "$FONT_DEST_DIR" ';'

