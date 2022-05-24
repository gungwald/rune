/*
 * stringconcat.c
 *
 *  Created on: Aug 8, 2016
 *      Author: Bill.Chatfield
 */

#include <stdio.h>
#include <stdarg.h>	/* va_arg, va_start */
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <wchar.h>	/* _wperror */
#include "string-concat.h"

 /**
  * Concatenates s & t, just like the plus operator in Java. The return
  * value is malloc'd so the caller must free it.
  */
wchar_t* concat(const wchar_t* s, const wchar_t* t)
{
    const wchar_t* sNotNull;
    const wchar_t* tNotNull;
    wchar_t* result;
    size_t length;

    if (s == NULL) {
        sNotNull = L"";
    }
    else {
        sNotNull = s;
    }

    if (t == NULL) {
        tNotNull = L"";
    }
    else {
        tNotNull = t;
    }

    /* Variables s & t are guaranteed to be non-NULL at this point. */
    length = wcslen(sNotNull) + wcslen(tNotNull);

    /* Calculate the exact size of the resulting string when s & t are
       concatenated, plus 1 for the string terminator character. */
    result = (wchar_t*)malloc((length + 1) * sizeof(wchar_t));

    if (result != NULL) {
        /* There is no need for special "secure" string functions because
           the size of result has already been correctly calculated so
           a buffer overrun is NOT POSSIBLE. */
        wcscpy(result, sNotNull);
        wcscat(result, tNotNull);
    }
    else {
        _wperror(L"concat");
    }
    return result;
}

wchar_t* concatv(const wchar_t* first, ...)
{
    va_list argPtr;
    wchar_t* s;
    size_t totalLength;
    wchar_t* result;

    /* Calculate total length of result string. */
    totalLength = wcslen(first);
    va_start(argPtr, first);
    while ((s = va_arg(argPtr, wchar_t*)) != NULL) {
        totalLength += wcslen(s);
    }
    va_end(argPtr);

    result = (wchar_t*)malloc((totalLength + 1) * sizeof(wchar_t));
    if (result != NULL) {
        va_start(argPtr, first);
        wcscpy(result, first);
        while ((s = va_arg(argPtr, wchar_t*)) != NULL) {
            wcscat(result, s);
        }
        va_end(argPtr);
    }
    return result;
}
/**
 * Concatenates s & t, just like the plus operator in Java. The return
 * value is malloc'd so the caller must free it.
 */
wchar_t* concat3(const wchar_t* s, const wchar_t* t, const wchar_t* u)
{
    const wchar_t* sNotNull;
    const wchar_t* tNotNull;
    const wchar_t* uNotNull;
    wchar_t* result;
    size_t length;

    if (s == NULL) {
        sNotNull = L"";
    }
    else {
        sNotNull = s;
    }

    if (t == NULL) {
        tNotNull = L"";
    }
    else {
        tNotNull = t;
    }

    if (u == NULL) {
        uNotNull = L"";
    }
    else {
        uNotNull = u;
    }

    /* Variables s & t are guaranteed to be non-NULL at this point. */
    length = wcslen(sNotNull) + wcslen(tNotNull) + wcslen(uNotNull);

    /* Calculate the exact size of the resulting string when s & t are
       concatenated, plus 1 for the string terminator character. */
    result = (wchar_t*)malloc((length + 1) * sizeof(wchar_t));

    if (result != NULL) {
        /* There is no need for special "secure" string functions because
           the size of result has already been correctly calculated so
           a buffer overrun is NOT POSSIBLE. */
        wcscpy(result, sNotNull);
        wcscat(result, tNotNull);
        wcscat(result, uNotNull);
    }
    else {
        _wperror(L"concat3");
    }
    return result;
}

wchar_t* concatUInt32(const wchar_t* s, uint32_t n)
{
    wchar_t numbertext[32];

    /* There can be no buffer overrun because a uint32_t cannot
       exceed the number of characters in the buffer. */
    _swprintf(numbertext, L"%u", n);
    return concat(s, numbertext);
}
