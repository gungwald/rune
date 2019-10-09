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

/** fs is assigned an object of type FileSystemObject */
var fs = new ActiveXObject("Scripting.FileSystemObject");
/** shell is assigned an object of type WshShell */
var shell = new ActiveXObject("WScript.Shell");
/** env is assigned an object of type WshEnvironment */
var env = shell.Environment("Process");

var mainClass = "com.alteredmechanism.notepad.Notepad";
var argumentString = buildArgumentString(WScript.Arguments);
var binDir = fs.GetParentFolderName(WScript.ScriptFullName);
var topDir = fs.GetParentFolderName(binDir);
var rsrcDir = fs.BuildPath(topDir, "resources");
var classesDir = fs.BuildPath(topDir, "classes");
var classpath = '"' + classesDir + ";" + rsrcDir + '"';
var spc = " ";
var javaHome = env("JAVA_HOME");
var javaExe;
if (javaHome) {
    var javaBinDir = fs.BuildPath(javaHome, "bin");
    java = fs.BuildPath(javaBinDir, "javaw.exe");
    java = '"' + java + '"';
} else {
    java = "javaw.exe";
}
var cmd = java + " -XX:OnOutOfMemoryError=^"handleOutOfMemoryError^" -classpath " + classpath + spc + mainClass + spc 
    + argumentString;

shell.Run(cmd);

