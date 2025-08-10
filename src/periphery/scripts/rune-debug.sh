#!/bin/sh

SCRIPT_DIR=`dirname "$0"` && [ "$SCRIPT_DIR" = "." ] && SCRIPT_DIR=`pwd`

bash -x "$SCRIPT_DIR"/rune "$@" > "$HOME"/rune-startup.log 2>&1
