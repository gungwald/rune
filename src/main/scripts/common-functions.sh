# Common functions for Rune scripts

# Backtick syntax is preferred over $(...) because it works on old systems.
# So disable the warning. This must come before any command to disable warnings
# in the whole file.
# shellcheck disable=SC2006

THIS_SCRIPT=`basename "$0" .sh`
SCRIPT_DIR=`dirname "$0"` && [ "$SCRIPT_DIR" = "." ] && SCRIPT_DIR=`pwd`
SCRIPT_FULL_NAME="$SCRIPT_DIR"/"$THIS_SCRIPT"
DTKSH_DIALOG="$SCRIPT_DIR"/dtksh-dialog

endsWith() (
  SEARCH_IN="$1"
  SEARCH_FOR="$2"
  case "$SEARCH_IN" in
    *"$SEARCH_FOR")
      true
      ;;
    *)
      false
      ;;
  esac
)

# $1 - Command basename
commandExists() ( # Parentheses make variables local to function.
  # The "which" command does not return 1 on failure in OSX 10.4.11.
  # So "type" must be used instead.
  type "$1" >/dev/null
)

displayErrorOnMac() ( # Parentheses make variables local to function.
  # Tell the background process SystemUIServer to display the dialog.
  osascript <<-EOF
		tell application "SystemUIServer"
			activate
			display dialog "$*" buttons {"OK"} with title "$TITLE"
		end tell
		activate application (path to frontmost application as text)
EOF
)

displayError() ( # Parentheses make variables local to function.
  TITLE="ERROR in Program: $SCRIPT_FULL_NAME"
  if [ "`uname -s`" = "Darwin" ]; then
    # This needs to be tried first because OS X could have xterm but it
    # won't get displayed if the X Windows System isn't running...
    displayErrorOnMac "$*"
  elif commandExists zenity; then
    zenity --text-info --title="$TITLE" --window-icon=error --text="$*"
  elif commandExists kdialog; then
    kdialog --title "$TITLE" --text "$*"
  elif commandExists dtksh && [ -x "$DTKSH_DIALOG" ]; then
    "$DTKSH_DIALOG" -t "$TITLE" -m "$*"
  elif commandExists Xdialog; then
    Xdialog --title="$TITLE" --screen-center --no-cancel --text "$*"
  elif commandExists xmessage; then
    # xmessage seems to automatically handle setting the width and height
    # properly. It's really the best of these programs... :-)
    xmessage -fg red -center -default okay -title "$TITLE" "$*"
  elif commandExists xterm; then
    xterm -title "$TITLE" -e "echo $* ; echo Press Enter to continue. ; read LINE"
  else
    echo "$TITLE"
    echo "$*"
  fi
)

displayErrorFile() ( # Parentheses make variables local to function.
  PROGRAM_NAME="$1"
  ERROR_FILE="$2"
  TITLE="ERROR in Program: $PROGRAM_NAME"
  WIDTH=900
  HEIGHT=480
  if [ "$(uname -s)" = "Darwin" ]; then
    # This needs to be tried first because OS X could have xterm but it
    # won't get displayed if the X Windows System isn't running...
    displayErrorOnMac "$(cat "$ERROR_FILE")"
  elif commandExists zenity; then
    zenity --text-info --height=$HEIGHT --width=$WIDTH --title="$TITLE" \
      --no-wrap --window-icon=error --filename="$ERROR_FILE"
  elif commandExists kdialog; then
    kdialog --title "$TITLE" --textbox "$ERROR_FILE" \
      --geometry ${WIDTH}x$HEIGHT
  elif commandExists dtksh && [ -x "$DTKSH_DIALOG" ]; then
    "$DTKSH_DIALOG" -t "$TITLE" -f "$ERROR_FILE"
  elif commandExists Xdialog; then
    Xdialog --title="$TITLE" --screen-center --no-cancel \
      --textbox "$ERROR_FILE" $HEIGHT $WIDTH
  elif commandExists xmessage; then
    # xmessage seems to automatically handle setting the width and height
    # properly. It's really the best of these programs... :-)
    xmessage -fg red -center -default okay -title "$TITLE" \
      -file "$ERROR_FILE"
  elif commandExists xterm; then
    xterm -title "$TITLE" \
      -e "cat $ERROR_FILE ; echo Press Enter to continue. ; read LINE"
  else
    echo "$TITLE"
    cat "$ERROR_FILE"
  fi
)

removeFiles() ( # Parentheses make variables local to function.
  for FILE_TO_REMOVE in "$@"; do
    # These conditions were added for troubleshooting failure
    # scenarios that actually occurred. They are not frivolous.
    if [ -n "$FILE_TO_REMOVE" ]; then
      if [ -f "$FILE_TO_REMOVE" ] || [ -p "$FILE_TO_REMOVE" ]; then
        rm -f "$FILE_TO_REMOVE"
      else
        displayError "$0": "$FILE_TO_REMOVE" does not exist, can\'t remove
      fi
    else
      displayError "$0": received empty file name parameter
    fi
  done
)

makeTempFile() ( # Parentheses make variables local to function.
  DESC="$1"
  TEMP_DIR=${TMPDIR:-/tmp}
  # Solaris 8 actually does not have mktemp...
  if commandExists mktemp; then
    # MacOS 10.4.11 requires this mktemp syntax.
    TEMP_FILE=`mktemp "$TEMP_DIR"/"$THIS_SCRIPT"."$DESC".XXXXXXXXXX` || exit
  else
    TEMP_FILE="$TEMP_DIR"/"$THIS_SCRIPT"."$DESC".$$.`date +%Y%m%d%H%M%S`
    touch "$TEMP_FILE" || exit
  fi
  echo "$TEMP_FILE"
)

quoteOrNothing() ( # Parentheses make variables local to function.
  if [ -n "$1" ]; then
    echo "\"$1\""
  fi
)

# Determines if $1 is less than $2, where $1 and $2 are floating point numbers
# represented as strings.
#
# Returns: true,  if $1 <  $2
#          false, if $1 >= $2
isLessThan() ( # Parentheses make variables local to function.
  if [ $# -ne 2 ]; then
    displayError "$0": wrong number of arguments: $#
    exit 1
  fi
  # Old versions of 'bc', like in Solaris 8, need the less-than condition
  # to be inside an 'if' statement.
  CONDITION="if ($1 < $2) \"true\""
  IS_LESS_THAN=`echo "$CONDITION" | bc`
  [ "$IS_LESS_THAN" = "true" ]
)

findJava() ( # Parentheses make variables local to function.
  unset JAVA # Necessary to determine if first "if" worked

  if [ -n "$JAVA_HOME" ]; then # JAVA_HOME is defined.
    JAVA_TEST="$JAVA_HOME"/bin/java
    if [ -e "$JAVA_TEST" ]; then
      if [ -x "$JAVA_TEST" ]; then
        JAVA="$JAVA_TEST"
      else
        displayError Java specified by JAVA_HOME is not executable: "$JAVA_TEST"
      fi
    else
      displayError Java specified by JAVA_HOME does not exist: "$JAVA_TEST"
    fi
  fi

  if [ -z "$JAVA" ]; then
    if commandExists java; then
      JAVA=java
    else
      displayError Java cannot be found in PATH: "$PATH"
      exit 1
    fi
  fi
  echo $JAVA
)

getJavaSpecVersion() ( # Parentheses make variables local to function.
  JAVA="$1"
  "$JAVA" -version 2>&1 | head -1 | cut -d ' ' -f 3 | tr -d '"' | cut -d . -f 1,2
)

isJavaLessThan_1_6() (
  JAVA="$1"
  isLessThan "`getJavaSpecVersion "$JAVA"`" "1.6"
)

