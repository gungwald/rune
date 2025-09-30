#!/bin/sh

# TODO - Keep the fonts in their original zip archive and extract them on demand
# TODO - Add support for Windows
# TODO - Keep the font cache updated (fc-cache -f -v)
# TODO - Keep the font cache updated on Mac (atsutil databases -removeUser)
# TODO - Keep the font cache updated on Haiku (makefont cache)

# shellcheck disable=SC2006
# shellcheck disable=SC2030
# shellcheck disable=SC2031

# Finds all .ttf & .otf files below the font source directory and
# copies them to the system/user font directory. Java 6 partially
# supports OpenType and Java 7 has full support.

# Written for: Mac, Linux, all BSDs, Haiku, Solaris, Illumos/OpenInd

# Designed to be POSIX compliant so that it works in bash, ash, dash

# Immediately exit the script if a command fails.
set -e

fail()
(
  echo "$@" 1>&2
  exit 1
)

getSystemFontDir()
(
    # Parentheses above make all variables local to this function.
    if [ "$OS" = 'darwin' ]
    then
        SYSTEM_FONT_DIR='/Library/Fonts'
    elif [ "$OS" = 'Linux' ] || [ "$OS" = 'OpenBSD' ] || [ "$OS" = 'NetBSD' ] || [ "$OS" = 'FreeBSD' ]
    then
        SYSTEM_FONT_DIR="$HOME/.fonts"
    elif [ "$OS" = 'SunOS' ]
    then
        if [ "$OS_VER" = '5.8' ] # Solaris 8 with CDE
        then
            SYSTEM_FONT_DIR='/usr/openwin/lib/X11/fonts/TrueType'
        fi
    elif [ "$OS" = 'Haiku' ]
    then
        SYSTEM_FONT_DIR="$HOME/config/non-packaged/data/fonts"
    else
        fail Unknown operating system: "$OS". Cannot determine font directory. 1>&2
    fi
    echo "$SYSTEM_FONT_DIR"
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
    FONT_DEST_DIR="$2"
    if [ "$OS" = 'darwin' ]
    then
        osascript <<'APPLE_SCRIPT' | grep -q "$FONT"
            use framework "AppKit"
            set fontFamilyNames to (current application's NSFontManager's sharedFontManager's availableFontFamilies) as list
            return fontFamilyNames
APPLE_SCRIPT
    elif [ "$OS" = 'SunOS' ] && [ "$OS_VER" = '5.8' ]
    then
        /usr/dt/bin/sdtfontadm | grep -q "$FONT"
    else
        if type fc-list > /dev/null
        then
          if fc-list | grep -q "$FONT"
          then
            true
          else
            if [ -f "$FONT_DEST_DIR"/"$FONT" ]
            then
              echo ERROR: font unknown but file found: "$FONT_DEST_DIR"/"$FONT". Will overwrite.
              false
            else
              false
            fi
          fi
        else
          fail The command fc-list is not installed.
        fi
    fi
)

installFont()
(
    # Parentheses above make all variables local to this function.
    TARGET_FONT_DIR="$1"
    while read -r FONT
    do
      FONT_SHORT_NAME=`basename "$FONT"`
      if isFontInstalled "$FONT_SHORT_NAME" "$TARGET_FONT_DIR"
      then
          echo Skipping already installed font: "$FONT_SHORT_NAME"
      else
          cp -pv "$FONT" "$TARGET_FONT_DIR"
      fi
    done
)

installFonts()
(
    # Parentheses above make all variables local to this function.
    FONT_SRC_DIR="$1"
    FONT_DEST_DIR="$2"
    if [ ! -d "$FONT_DEST_DIR" ]; then
        mkdir -p "$FONT_DEST_DIR"
    fi
    # Find TrueType or OpenType font files.
    find "$FONT_SRC_DIR" -name '*.[ot]tf' -print | installFont "$FONT_DEST_DIR"
)

OS=`uname -s`
OS_VER=`uname -r`
SELF=`getAbsolutePath "$0"`
MY_DIR=`dirname "$SELF"`
FONT_SRC_DIR=`dirname "$MY_DIR"`/fonts
FONT_DEST_DIR=`getSystemFontDir`

installFonts "$FONT_SRC_DIR" "$FONT_DEST_DIR"