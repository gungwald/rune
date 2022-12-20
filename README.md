# Rune

## Features

- Any OS supporting [Java 5](https://en.wikipedia.org/wiki/Java_version_history#J2SE_5.0) or newer can run it:
    - Windows 95, 98, ME, XP, Vista, 7, 8 and 10 (x86 32-bit and 64-bit)
    - Windows 10 AArm64 with [Microsoft's OpenJDK](https://www.microsoft.com/openjdk) build
    - Mac OS 10.4 and newer
    - Linux
    - NetBSD, OpenBSD, FreeBSD
    - Solaris 8 and newer
    - OpenIndiana (Illumos)
    - OS/2 Warp 4 with OpenJDK 6 from https://trac.netlabs.org/java
- Tested on:
    - Windows 10
    - Mac OS 10.4
    - Fedora Linux 34, 35
    - Ubuntu Linux
    - OpenBSD, NetBSD
    - Solaris 8 UltraSparc 64-bit
    - OpenIndiana 2021.10
    - OS/2 Warp 4
- Theme-able, but the default theme is consistent with your operating system
- Multiple files can be open at the same time in different tabs
- Start-up time is reasonably fast: about 3 seconds on a modern machine
- Anti-aliased (smooth) text
- Nice bundled free fonts for programming
- Zoom with Ctrl-Up and Ctrl-Down

## Planned Features

- Search
- Syntax highlighting
- Ability to save state across process restarts
- Project files
- Vim emulation
- New platforms
  - Haiku
  - Raspberry Pi
  - RiscOS? I don't think it supports Java but not sure.
  - Amiga with [JAmiga](http://os4depot.net/?function=showfile&file=development/language/jamiga.lha)
  - Atari
  - ReactOS
  - MorphOS (Amiga)

## Downloads

https://github.com/gungwald/rune/releases

## Requirements

Any hardware and operating system that offers Java 5 and up, so it works on old machines and new machines.

## Startup Instructions

### Mac (10.4 and up)

    Mount .dmg file.
    Copy Rune.app to Application folder.
    Double-click Rune.app

### Windows

I am still working on the installer.

    1. Make sure Java is installed. Any version 5 and above will work.
    	a. For Intel/AMD you can get it from https://java.sun.com
    	b. For ARM you can get it from https://www.microsoft.com/openjdk
    2. Run this from the command line: java -jar rune.jar

### Linux, BSD, Solaris, OpenIndiana

    Install some version of openjdk. Any version available will work.
    Run this from the command line: java -jar rune.jar

### OS/2

    Install Java 6 from https://trac.netlabs.org/java
    Run Rune from the command line: java -jar rune.jarchines and new machines.

### Haiku

Not everything works right. For example, only one file can be opened. I'm not sure it this is caused by bugs in the Haiku Java port, or bugs in Rune that can't be seen on other platforms. I will try to look into it when I have time. There are also issues with memory that require some extra parameters on the Java command line. So it is necessary to create a script to start up Rune.

    Install any available version of openjdk from HaikuDepot.
    Create a script like the following, replacing the value of JAVA_HOME with your version of Java:

