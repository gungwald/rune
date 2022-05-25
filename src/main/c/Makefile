
# The OS environment variable is defined on Windows.
#
ifeq ($(OS),Windows_NT)
	# These commands are what you get when you use scoop to install gcc.
	CC=gcc
	STRIP=strip
else
	# These command are what you get on Fedora and MacOS for the cross 
	# compiler.
	CC=i686-w64-mingw32-gcc
	STRIP=i686-w64-mingw32-strip
endif

CFLAGS=-O3 -DUNICODE -D_UNICODE -D_CRT_SECURE_NO_WARNINGS -municode 
LDFLAGS=-municode

all: rune-launcher.exe rune-out-of-memory-error-handler.exe


rune-launcher.exe: rune-launcher.o errors.o string-concat.o
	$(CC) $(LDFLAGS) $^ -o $@
	$(STRIP) $@

rune-out-of-memory-error-handler.exe: rune-out-of-memory-error-handler.o
	$(CC) $(LDFLAGS) $^ -o $@
	$(STRIP) $@

rune-launcher.o: rune-launcher.c errors.h string-concat.h


errors.o: errors.c errors.h


string-concat.o: string-concat.c string-concat.h


rune-out-of-memory-error-handler.o: rune-out-of-memory-error-handler.c


clean:
	$(RM) *.exe *.o
