' Source - https://superuser.com/a/838380
' Posted by Anthony Kong
' Retrieved 2026-07-14, License - CC BY-SA 3.0

Dim WinFontDir
Dim SrcFontDir
WinFontDir = "C:\Windows\Fonts"
SrcFontDir = "..\fonts"

Set objShell = CreateObject("Shell.Application")
Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objSrc = objFSO.GetFolder(SrcFontDir)
Set colFiles = objSrc.Files
For each objFile in colFiles
    If Not objFSO.FileExists(WInFontDir + "\" + objFile.Name) Then
        WScript.Echo "Copying " + objFile.Name
        FONTS = &H14&
        Set objFontFolder = objShell.Namespace(FONTS)
        objFontFolder.CopyHere objFile.Path
    End
Next

