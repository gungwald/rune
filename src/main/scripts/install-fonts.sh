#!/bin/sh

# Finds all .ttf & .otf files below the font source directory and
# copies them to the system/user font directory. Java 6 partially
# supports OpenType and Java 7 has full support.

# shellcheck disable=SC2006

getSystemFontDir()
(
    # Parentheses above make all variables local to this function.
    if [ "$OS" = 'darwin' ]
    then
        SYSTEM_FONT_DIR='/Library/Fonts'
    elif [ "$OS" = 'Linux' ]
    then
        SYSTEM_FONT_DIR="$HOME/.fonts"
    else
        echo Unknown operating system: $OS. Cannot determine font directory. 1>&2
        exit 1
    fi
    echo $SYSTEM_FONT_DIR
)

getAbsolutePath()
(
    # Parentheses above make all variables local to this function.
    SHORT_NAME="$1"
    if [ -d "$SHORT_NAME" ]
    then
        ( cd "`dirname "$SHORT_NAME"`" || exit
          pwd )
    else
        ( cd "`dirname "$SHORT_NAME"`" || exit
          echo "`pwd`"/"`basename "$SHORT_NAME"`" )
    fi
)

isFontInstalled()
(
    # Parentheses above make all variables local to this function.
    FONT="$1"
    if [ "$OS" = 'darwin' ]
    then
        osascript <<'SCPT' | grep -q "$FONT"
            use framework "AppKit"
            set fontFamilyNames to (current application's NSFontManager's sharedFontManager's availableFontFamilies) as list
            return fontFamilyNames
SCPT
    else
        fc_list | grep -q "$FONT"
    fi
)

installFont()
(
    # Parentheses above make all variables local to this function.
    FONT="$1"
    TARGET_FONT_DIR="$2"
    if isFontInstalled "$FONT"
    then
        echo $FONT is alreaady installed. Skipping...
    else
        cp -ipv "$FONT" "$TARGET_FONT_DIR"
    fi
)

installFonts()
(
    # Parentheses above make all variables local to this function.
    FONT_SRC_DIR="$1"
    FONT_DEST_DIR="$2"
    if [ ! -d "$FONT_DEST_DIR" ]; then
        mkdir -p "$FONT_DEST_DIR"
    fi
    find "$FONT_SRC_DIR" -name '*.ttf' -name '*.otf' \
                -exec installFont '{}' "$FONT_DEST_DIR" ';'
)

OS=`uname -s`
SELF=`getAbsolutePath "$0"`
MY_DIR=`dirname "$SELF"`
FONT_SRC_DIR=`dirname "$MY_DIR"`/resources/fonts
FONT_DEST_DIR=`getSystemFontDir`

installFonts "$FONT_SRC_DIR" "$FONT_DEST_DIR"

