#!/bin/sh

# Backtick syntax is preferred over $(...) because it works on old systems.

# shellcheck disable=SC2006
SCRIPT_DIR=`dirname "$0"` && [ "$SCRIPT_DIR" = "." ] && SCRIPT_DIR=`pwd`
. "$SCRIPT_DIR"/common-functions.sh
displayError Rune ran out of memory.
