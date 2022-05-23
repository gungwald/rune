#include <stdlib.h>
#include <windows.h>

int WINAPI wWinMain(HINSTANCE instance, HINSTANCE prevInstance, LPWSTR commandLine, int commandShow)
{
	MessageBoxW(
		NULL, 
		L"An OutOfMemoryError has occurred with Rune. It will now be closed.", 
		NULL,
		MB_OK);

	/* Get command line, convert to argv, get pid argument, kill process */

	return EXIT_SUCCESS;
}

