// rune-launcher.c: Defines the entry point for the application.
//

#include <windows.h>
#include "resources.h"
#include "errors.h"
#include "string-concat.h"

#define MAX_LOADSTRING 100

// Global Variables:
HINSTANCE hInst;                                // current instance
WCHAR szTitle[MAX_LOADSTRING];                  // The title bar text
WCHAR szWindowClass[MAX_LOADSTRING];            // the main window class name

// Forward declarations of functions included in this code module:
ATOM                MyRegisterClass(HINSTANCE hInstance);
BOOL                InitInstance(HINSTANCE, int);
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK    About(HWND, UINT, WPARAM, LPARAM);

wchar_t *dirname(const wchar_t *path);
wchar_t *basenameNoExtension(const wchar_t *path);
wchar_t *buildPath(const wchar_t *basePath, const wchar_t *elementToAppend);
void exec(wchar_t *cmdLine);

int APIENTRY wWinMain(_In_ HINSTANCE hInstance,
                     _In_opt_ HINSTANCE hPrevInstance,
                     _In_ LPWSTR    lpCmdLine,
                     _In_ int       nCmdShow)
{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

    wchar_t fullProgramName[MAX_PATH];

    GetModuleFileNameW(NULL, fullProgramName, MAX_PATH);
    wchar_t *programDirectory = dirname(fullProgramName);
    wchar_t* parentDirectory = dirname(programDirectory);
    wchar_t* libDirectory = buildPath(parentDirectory, L"lib");
    wchar_t* simpleName = basenameNoExtension(fullProgramName);
    wchar_t* jarSimpleName = concat(simpleName, L".jar");
    wchar_t* jar = buildPath(libDirectory, jarSimpleName);
    // TODO - Append command line arguments
    wchar_t* commandLine = concat(L"javaw -jar ", jar);

    exec(commandLine);

    free(commandLine);
    free(jarSimpleName);
    free(jar);
    free(simpleName);
    free(libDirectory);
    free(parentDirectory);
    free(programDirectory);

    // Initialize global strings
//    LoadStringW(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
//    LoadStringW(hInstance, IDC_RUNELAUNCHER, szWindowClass, MAX_LOADSTRING);
//    MyRegisterClass(hInstance);

    // Perform application initialization:
//    if (!InitInstance (hInstance, nCmdShow))
//    {
//        return FALSE;
//    }

//    HACCEL hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_RUNELAUNCHER));

//    MSG msg;

    // Main message loop:
//    while (GetMessage(&msg, NULL, 0, 0))
//    {
//        if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
//        {
//            TranslateMessage(&msg);
//            DispatchMessage(&msg);
//        }
//    }

 //   return (int) msg.wParam;
}

/**
 * Implements POSIX dirname function. Result must be free'd.
 */
wchar_t *dirname(const wchar_t *path)
{
    wchar_t *dirname = _wcsdup(path);
    wchar_t *lastBackslash = wcsrchr(dirname, L'\\');
    if (lastBackslash) {
        lastBackslash[0] = L'\0';
    }
    return dirname;
}

wchar_t* basenameNoExtension(const wchar_t* path)
{
    wchar_t* basename = _wcsdup(path);
    wchar_t *lastBackslash = wcsrchr(basename, L'\\');
    if (lastBackslash) {
        wcscpy(basename, lastBackslash + 1);
    }
    wchar_t* lastDot = wcsrchr(basename, L'.');
    if (lastDot) {
        lastDot[0] = L'\0';
    }
    return basename;
}

/* Result must be free'd. */
wchar_t* buildPath(const wchar_t* basePath, const wchar_t* elementToAppend)
{
    return concat3(basePath, L"\\", elementToAppend);
}

void exec(wchar_t* commandLine)
{
    STARTUPINFO startupInfo;
    PROCESS_INFORMATION processInfo;

    ZeroMemory(&startupInfo, sizeof(startupInfo));
    startupInfo.cb = sizeof(startupInfo);
    ZeroMemory(&processInfo, sizeof(processInfo));

    if (CreateProcessW(NULL, commandLine, NULL, NULL, TRUE, CREATE_UNICODE_ENVIRONMENT, NULL, NULL, &startupInfo, &processInfo)) {
        // Wait until child process exits.
        WaitForSingleObject(processInfo.hProcess, INFINITE);
        CloseHandle(processInfo.hProcess);
        CloseHandle(processInfo.hThread);
    } 
    else {
        wchar_t* systemMessage = getErrorMessage(GetLastError());
        wchar_t* message = concatv(L"Failed to start process:\n\n", commandLine, L"\n\n", systemMessage, NULL);
        MessageBoxW(NULL, message, NULL, MB_OK | MB_APPLMODAL | MB_ICONERROR);
        free(message);
        free(systemMessage);
    }
}

//
//  FUNCTION: MyRegisterClass()
//
//  PURPOSE: Registers the window class.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
    WNDCLASSEXW wcex;

    wcex.cbSize = sizeof(WNDCLASSEX);

    wcex.style          = CS_HREDRAW | CS_VREDRAW;
    wcex.lpfnWndProc    = WndProc;
    wcex.cbClsExtra     = 0;
    wcex.cbWndExtra     = 0;
    wcex.hInstance      = hInstance;
    wcex.hIcon          = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_RUNELAUNCHER));
    wcex.hCursor        = LoadCursor(NULL, IDC_ARROW);
    wcex.hbrBackground  = (HBRUSH)(COLOR_WINDOW+1);
    wcex.lpszMenuName   = MAKEINTRESOURCEW(IDC_RUNELAUNCHER);
    wcex.lpszClassName  = szWindowClass;
    wcex.hIconSm        = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

    return RegisterClassExW(&wcex);
}

//
//   FUNCTION: InitInstance(HINSTANCE, int)
//
//   PURPOSE: Saves instance handle and creates main window
//
//   COMMENTS:
//
//        In this function, we save the instance handle in a global variable and
//        create and display the main program window.
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   hInst = hInstance; // Store instance handle in our global variable

   HWND hWnd = CreateWindowW(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);

   if (!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE: Processes messages for the main window.
//
//  WM_COMMAND  - process the application menu
//  WM_PAINT    - Paint the main window
//  WM_DESTROY  - post a quit message and return
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch (message)
    {
    case WM_COMMAND:
        {
            int wmId = LOWORD(wParam);
            // Parse the menu selections:
            switch (wmId)
            {
            case IDM_ABOUT:
                DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
                break;
            case IDM_EXIT:
                DestroyWindow(hWnd);
                break;
            default:
                return DefWindowProc(hWnd, message, wParam, lParam);
            }
        }
        break;
    case WM_PAINT:
        {
            PAINTSTRUCT ps;
            HDC hdc = BeginPaint(hWnd, &ps);
            // TODO: Add any drawing code that uses hdc here...
            EndPaint(hWnd, &ps);
        }
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        break;
    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

// Message handler for about box.
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    UNREFERENCED_PARAMETER(lParam);
    switch (message)
    {
    case WM_INITDIALOG:
        return (INT_PTR)TRUE;

    case WM_COMMAND:
        if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
        {
            EndDialog(hDlg, LOWORD(wParam));
            return (INT_PTR)TRUE;
        }
        break;
    }
    return (INT_PTR)FALSE;
}
