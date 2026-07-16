#!/bin/sh

# TODO - Keep the fonts in their original zip archive and extract them on demand
# TODO - Add support for Windows

# shellcheck disable=SC2006
# shellcheck disable=SC2030
# shellcheck disable=SC2031

# Finds all .ttf & .otf files below the font source directory and
# copies them to the system/user font directory. Java 6 partially
# supports OpenType and Java 7 has full support.

# So this should not be rewritten in a non-POSIX shell like fish.
# That would break compatibility with supported systems. And fish
# is stupid.
# It should not be written in a way that breaks compatibility with
# Java versions 6 or greater.

# Written for: Mac, Linux, all BSDs, Haiku, Solaris, Illumos/OpenInd
# If you're using Windows, then run with Git Bash or WSL.

# Designed to be POSIX compliant so that it works in bash, ash, dash,
# ksh, zsh, and other POSIX-compliant shells, not just bash.

# Immediately exit the script if a command fails.
set -e

fail()
(
  echo "$@" 1>&2
  exit 1
)

getSystemFontDir()
(
    # The parenthesis above makes all variables local to this function.
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
        fail Unknown operating system: "$OS". Cannot determine font directory.
    fi
    echo "$SYSTEM_FONT_DIR"
)

getAbsolutePath()
(
    # The parenthesis above makes all variables local to this function.
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
    # The parenthesis above makes all variables local to this function.
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
    # The parenthesis above makes all variables local to this function.
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

updateFontCache()
(
    # The parenthesis above makes all variables local to this function.
    FONT_DIR="$1"
    if [ "$OS" = 'darwin' ]
    then
      # NOT TESTED - Please report if this works.
      atsutil databases -removeUser
    elif [ "$OS" = 'SunOS' ]
    then
      # Should include 5.8 and previous versions without CDE as well.
      if [ "$OS_VER" = '5.8' ]
      then
        # On Solaris 8 with CDE, update the font cache.
        # NOT TESTED - Please report if this works.
        /usr/dt/bin/sdtfontadm
      else
        # On Solaris 9 and later, the font cache is updated automatically.
        # Manually installed fonts should be available immediately.
        :
      fi
    elif [ "$OS" = 'Haiku' ]
    then
        # On Haiku, the font cache is updated automatically.
        # Manually installed fonts should be available immediately but
        # maybe a restart is needed in some cases. Hence,
        # we try to force a cache update here.
        # NOT TESTED - Please report if this works.
        /system/bin/makefont cache
    elif [ "$OS" = 'Linux' ] || [ "$OS" = 'OpenBSD' ] || [ "$OS" = 'NetBSD' ] || [ "$OS" = 'FreeBSD' ]
    then
      # TODO - What if it's not in the PATH?
      if type fc-cache > /dev/null
      then
          fc-cache --error-on-no-fonts --force --verbose "$FONT_DIR"
      fi
    elif [ "$OS" = 'SunOS' ]
    then
        # On Solaris 9 and later, the font cache is updated automatically.
        # Manually installed fonts should be available immediately.
        :
    else
        fail Unknown operating system: "$OS". Cannot update font cache.
    fi
)

installFonts()
(
    # The parenthesis above makes all variables local to this function.
    FONT_SRC_DIR="$1"
    FONT_DEST_DIR="$2"
    if [ ! -d "$FONT_DEST_DIR" ]; then
        mkdir -p "$FONT_DEST_DIR"
    fi
    # Find TrueType or OpenType font files.
    find "$FONT_SRC_DIR" -name '*.[ot]tf' -print | installFont "$FONT_DEST_DIR"
    updateFontCache "$FONT_DEST_DIR"
)

OS=`uname -s`
OS_VER=`uname -r`
SELF=`getAbsolutePath "$0"`
MY_DIR=`dirname "$SELF"`
FONT_SRC_DIR=`dirname "$MY_DIR"`/fonts
FONT_DEST_DIR=`getSystemFontDir`

# For Windows, call out to a different script.
if isRunningOnWindows
then
    echo Starting Windows PowerShell script to install fonts: install-fonts.ps1
    powershell install-fonts.ps1 "$FONT_SRC_DIR"
else
    installFonts "$FONT_SRC_DIR" "$FONT_DEST_DIR"
fi
