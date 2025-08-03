#!/bin/sh

# Gallant is the font used by the console of Sun Microsystems' workstations.

# This script sticks to older ways of doing things and older parameters
# so that it continues to work on older operating systems and hardware.

RESPONSE_HEADER_FILE="$HOME"/response-headers-for-sun-console-font-download.txt

if which curl > /dev/null
then
	curl --insecure --location --dump-header "$RESPONSE_HEADER_FILE" \
	  --progress-bar --create-dirs \
	  --output gallant12x22.ttf \
		https://archive.org/download/gallant12x22/gallant12x22.ttf

	echo Response headers written to "$RESPONSE_HEADER_FILE"
else
	echo ERROR: Curl not found. Please install it.
fi
