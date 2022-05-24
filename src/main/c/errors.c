/*
 * errors.c
 *
 *  Created on: Aug 8, 2016
 *      Author: Bill.Chatfield
 */

#include <stdio.h>
#include <lmerr.h>
#include <windows.h>

#include "string-concat.h"
#include "errors.h"

void printErrorMessage(const wchar_t* offendingObject, unsigned long errorCode)
{
    wchar_t* message;

    message = getErrorMessage(errorCode);
    fwprintf(stderr, L"which: \u201C%s\u201D: %s\n", offendingObject, message);
    free(message);
}

/* This function was taken from Microsoft's Knowledge Base Article 149409
   and modified to fix the formatting. The returned string must be free'd
   by the caller. */
wchar_t* getErrorMessage(unsigned long errorCode)
{
    HMODULE messageLocation;
    wchar_t* message;
    wchar_t* finalMessage;
    unsigned long messageLength;

    /* If dwLastError is in the network range, load the message source. */
    if (NERR_BASE <= errorCode && errorCode <= MAX_NERR) {
        messageLocation = LoadLibraryEx(L"netmsg.dll", NULL, LOAD_LIBRARY_AS_DATAFILE);
    }
    else {
        /* NULL means to look for the message in the system module. */
        messageLocation = NULL;
    }

    /* Call FormatMessage to allow for message text to be acquired
       from the system or the supplied module handle. */
    messageLength = FormatMessageW(
        FORMAT_MESSAGE_ALLOCATE_BUFFER |
        FORMAT_MESSAGE_IGNORE_INSERTS |
        FORMAT_MESSAGE_FROM_SYSTEM | /* Always consider system table */
        ((messageLocation != NULL) ? FORMAT_MESSAGE_FROM_HMODULE : 0),
        messageLocation, /* Module to get message from (NULL == system) */
        errorCode,
        MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), /* Default language */
        (wchar_t*)&message,
        0,
        NULL);

    if (messageLength == 0) {
        finalMessage = concatUInt32(L"Error code ", errorCode);
    }
    else {
        // Make a copy so that the caller can free the message with 'free'
        // instead of 'LocalFree'.
        finalMessage = _wcsdup(message);
        LocalFree(message);
    }
    /* If a message source was loaded, unload it. */
    if (messageLocation != NULL) {
        FreeLibrary(messageLocation);
    }
    return finalMessage;
}
