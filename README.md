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
  - IRIX
  - HP-UX
  - SerenityOS

## Downloads

https://github.com/gungwald/rune/releases

## Requirements

Any hardware and operating system that offers Java 5 and up, so it works on old machines and new machines.

## Startup Instructions

There are a few different forms of packaging. The simplest is a single .jar file.

    1. rune.jar (Works anywhere Java is installed)
    2. Rune.dmg (Mac only)
    3. Rune.zip (Zipped Rune.dmg)
    4. rune-portable.zip (Does not require an install. Just unzip where ever you want)
    5. rune-installer.jar (Cross-platform installer but does not work yet)

### Mac (10.4 and up)

#### The Mac Way

    1. Dowwnload Rune.dmg or Rune.zip. If you download Rune.zip, unzip it first.
    2. Double-click Rune.dmg or Rune.zip to mount it.
    3. Drag Rune.app to your Applications folder or wherever you want it.
    4. Double-click Rune.app to start it.

#### Other

The plain rune.jar file and rune-portable.zip also work on Mac OS. The
instructions are the same as for Windows, Linux, etc. below.

### Windows, Linux, BSD, Solaris, OpenIndiana

I am still working on the installer rune-installer.jar. So, there are
two options. You can use the portable version, or you can use the .jar file.

#### Portable version

    1. Download rune-portable.zip
    2. Unzip it wherever you want
    3. Make sure Java is installed. Any version 5 and above will work.
        a. Windows
            1. For Intel/AMD you can get it from https://java.sun.com
            2. For ARM you can get it from https://www.microsoft.com/openjdk
        b. Linux/BSD/Solaris/OpenIndiana
            1. Use your package manager to install openjdk-8-jre or newer
    4. Open the folder where you unzipped rune-portable.zip
    5. Open the "bin" folder
    6. Double-click rune.bat (Windows) or rune (Other) to start it
    7. (Optional) Add the "bin" folder to your PATH so you can run it from anywhere
    8. (Optional) Create a shortcut to rune.bat or rune on your desktop or start menu
    9. (Optional) Change the icon of the shortcut to rune.ico in the "bin" folder

#### Using the .jar file

    1. Make sure Java is installed. Any version 5 and above will work.
    	a. For Intel/AMD you can get it from https://java.sun.com
    	b. For ARM you can get it from https://www.microsoft.com/openjdk
    2. Run this from the command line: java -jar rune.jar
    3. (Optional) Create a shortcut to run the above command on your desktop or start menu
    4. (Optional) Create a batch file or shell script to run the above command

#### (Optional) Installing Fonts (rune-portable.zip only)

For UN`X-like systems (Linux, BSD, Solaris, OpenIndiana) you may want to install the
fonts that come with Rune. Run the install-fonts.sh script.

    1. Open a terminal
    2. Change to the "bin" directory inside the unzipped rune-portable.zip
    3. Run this command: ./install-fonts.sh
    4. You may need to restart your X server or your computer
    5. The fonts will be installed in ~/.fonts. You should be able to select them inside Rune and other applications as well.
 
### OS/2

#### Portable version

    1. Download rune-portable.zip
    2. Unzip it wherever you want
    1. Install Java 6 from https://trac.netlabs.org/java
    2. Add the Java bin directory to your PATH
    3. You may need to restart
    4. Open the folder where you unzipped rune-portable.zip
    5. Open the "bin" folder
    6. Double-click rune.cmd to start it
    7. (Optional) Add the "bin" folder to your PATH so you can run it from anywhere
    8. (Optional) Create a shortcut to rune.bat or rune on your desktop or start menu

#### Using the .jar file

    1. Install Java 6 from https://trac.netlabs.org/java
    2. Add the Java bin directory to your PATH
    3. You may need to restart
    4. Run this from the command line: java -jar rune.jar

### Haiku

Not everything works right. For example, only one file can be opened. I'm not sure it this is caused by bugs in the Haiku Java port, or bugs in Rune that can't be seen on other platforms. I will try to look into it when I have time. There are also issues with memory that require some extra parameters on the Java command line. So it is necessary to create a script to start up Rune.

    1. Install any available version of openjdk from HaikuDepot.
    2. Add the Java install directory to your PATH. It will be
       something like /system/lib/openjdk/bin
    3. You may need to restart
    4. Run this from the command line: java -jar rune.jar

### Other

If your operating system supports Java 5 and up, you can run the plain rune.jar file
or rune-portable.zip. For rune.jar run this from the command line:

    java -jar rune.jar

Create a script to run this command and make it executable.

For rune-portable.zip, unzip it wherever you want, go to the "bin" folder and run
rune.

## Building from Source

You need [Apache Ant](https://ant.apache.org/) to build Rune from source.

    1. Install Java 5 or newer
    2. Install Apache Ant
    3. Download the source code from https://github.com/gungwald/rune
    4. Open a command line and change to the directory where you unzipped the source code
    5. Run "ant"
    6. The built files will be in the "target" directory
    7. You can run Rune from the command line with "java -jar target/rune.jar"
