// Runs Java notepad application without a flashing Command Prompt window.

function buildArgumentString(args)
{
	var argumentString = "";
	var i;
	for (i = 0; i < args.length; i++) {
		argumentString += '"' + args.Item(i) + '" ';
	}
    return argumentString;
}

var shell = new ActiveXObject("WScript.Shell");
var fs = new ActiveXObject("Scripting.FileSystemObject");

var mainClass = "com.alteredmechanism.notepad.Notepad";
var argumentString = buildArgumentString(WScript.Arguments);
var binDir = fs.GetParentFolderName(WScript.ScriptFullName);
var topDir = fs.GetParentFolderName(binDir);
var rsrcDir = fs.BuildPath(topDir, "resources");
var classesDir = fs.BuildPath(topDir, "classes");
var classpath = '"' + classesDir + ";" + rsrcDir + '"';
var spc = " ";

shell.Run("javaw -classpath " + classpath + spc + mainClass + spc + argumentString);
