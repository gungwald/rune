#include <stdlib.h>
#include <windows.h>

int WINAPI wWinMain(HINSTANCE instance, HINSTANCE prevInstance, LPWSTR commandLine, int commandShow)
{
	/* Get command line, convert to argv, get pid argument, kill process */
	int argCount;
	wchar_t **args;
       
	args = CommandLineToArgvW(GetCommandLineW(), &argCount);

	if (argCount > 1) {
		wchar_t *pid = args[1];
	}

	MessageBoxW(
		NULL, 
		L"An OutOfMemoryError has occurred with Rune. It will now be closed.", 
		NULL,
		MB_OK);

	return EXIT_SUCCESS;
}

