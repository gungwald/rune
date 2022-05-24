#pragma once
/*
 * errors.h
 *
 *  Created on: Aug 8, 2016
 *      Author: Bill.Chatfield
 */

#include <wchar.h>

extern wchar_t* getErrorMessage(unsigned long errorCode);
extern void printErrorMessage(const wchar_t* offendingObject, unsigned long errorCode);
