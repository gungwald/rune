
CC=i686-w64-mingw32-gcc
STRIP=i686-w64-mingw32-strip

CC=gcc
STRIP=strip

CFLAGS=-O3 -DUNICODE -D_UNICODE -D_CRT_SECURE_NO_WARNINGS -municode 
#-m32
LDFLAGS=-municode

all: rune-launcher.exe rune-out-of-memory-error-handler.exe


rune-launcher.exe: rune-launcher.o errors.o string-concat.o
	$(CC) $(LDFLAGS) $^ -o $@
	$(STRIP) $@

rune-launcher.o: rune-launcher.c errors.h string-concat.h


errors.o: errors.c errors.h


string-concat.o: string-concat.c string-concat.h


rune-out-of-memory-error-handler.exe: rune-out-of-memory-error-handler.c
	$(CC) $(CFLAGS) $< -o $@
	$(STRIP) $@

clean:
	$(RM) *.exe *.o
