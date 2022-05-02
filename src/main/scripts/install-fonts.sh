#!/bin/sh

# Finds all .ttf files below the directory this script is in and
# copies them to the system font directory.

getSystemFontDir()
(
    # shellcheck disable=SC2006
    OS=`uname -s`
    if [ "$OS" = 'darwin' ]; then
        SYSTEM_FONT_DIR='/Library/Fonts'
    else
        echo Unknown system $OS
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

# Find and install any TrueType fonts.
# shellcheck disable=SC2006
find "$MY_DIR" -name '*.ttf' -exec cp -ipv '{}' "`getSystemFontDir`" ';'

