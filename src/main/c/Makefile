
CC=i686-w64-mingw32-gcc
STRIP=i686-w64-mingw32-strip
CFLAGS=-O3 -DUNICODE -D_UNICODE -municode

rune-out-of-memory-error-handler.exe: rune-out-of-memory-error-handler.c
	$(CC) $(CFLAGS) $< -o $@
	$(STRIP) $@

clean:
	$(RM) *.exe *.o
