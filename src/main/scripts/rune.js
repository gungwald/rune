// Runs Java notepad application without a flashing Command Prompt window.

/**
 * Java doesn't expand wildcards in file names on the command line.
 * It expects the shell to do it, as is done in UNIX. But, Windows
 * don't do dat. So it has to be done manually so the expected
 * behavior of a Windows app is maintained.
 * 
 * @param {String} globulet - A string containing wildcards (a.k.a. a glob)
 * @returns {Array} - The file names specified by the glob/wildcards
 */
function expandGlob(globulet)
{
    // Run the 'dir' command to do the expanding for us.
    var command = 'cmd /c dir /s /b "' + globulet + '"';
    var globExpanderProcess = shell.Exec(command);
    // Collect the output of the 'dir' command. Each line of output becomes a
    // new element in the 'files' array.
    var input = globExpanderProcess.StdOut;
    var files = new Array();
    while (! input.AtEndOfStream) {
    	files.push(input.ReadLine());
    }
    // Throw an exception if it produced any errors.
    var errorStream = globExpanderProcess.StdErr;
    if (! errorStream.AtEndOfStream) {
    	throw new Error(errorStream.ReadAll());
    }
    // Wait for the process to exit.
    while (globExpanderProcess.Status == 0) {
    	WScript.Sleep(1000);
    }
    // Throw an exception if it failed.
    var exitCode = globExpanderProcess.ExitCode;
    if (exitCode != 0) {
    	throw new Error("Command failed with exit code " + exitCode + ": " + command);
    }
    return files;
}

function buildArgumentString(args)
{
    var argumentString = "";
    var i, j;
    for (i = 0; i < args.length; i++) {
        var arg = args.Item(i);
        if (arg.indexOf("*") >= 0) {
            var files = expandGlob(arg);
            for (j = 0; j < files.length; j++) {
            	argumentString += '"' + files[j] + '" ';
            }
        } else {
            argumentString += '"' + arg + '" ';
        }
    }
    return argumentString;
}

function isRunningWithCScript()
{
    return WScript.FullName.search(/CScript/i) >= 0;
}

/**
 * Finds the Java executable file that should be used.
 * 
 * @param {FileSystemObject} fs - Provides access to file system
 * @param {WshEnvironment} env - Provides access to environment variables
 * @returns {String} - The full path to javaw in JAVA_HOME or simply javaw
 * 						to be found in the PATH
 */
function findJava(fs, env)
{
    var java = null;
    
    var javaProgramToFind;
    if (isRunningWithCScript()) {
    	javaProgramToFind = "java.exe";
    } else {
    	javaProgramToFind = "javaw.exe";
    }

	var javaHomeEnvVars = [ 'RUNE_JAVA_HOME', 'JAVA_HOME' ];
	for (var i=0; i < javaHomeEnvVars.length; i++) {
		var javaHome = env(javaHomeEnvVars[i]);
		if (javaHome) {
			var javaBinDir = fs.BuildPath(javaHome, "bin");
			var testJava = fs.BuildPath(javaBinDir, javaProgramToFind);
			if (fs.FileExists(testJava)) {
				java = testJava;
				break;
			}
		}
	}
    if (java == null) {
    	java = javaProgramToFind;
    }
    return java;
}

/**
 * Copy all input data from inputStream to outputStream.
 * 
 * @param {TextStream} inputStream
 * @param {TextStream} outputStream
 * @returns NoThing
 */
function copyAll(inputStream, outputStream)
{
    while (! inputStream.AtEndOfStream) {
    	outputStream.WriteLine(inputStream.ReadLine());
    }
}

/**
 * Copies stdout and stderr from process to this script's output.
 * 
 * @param {WshScriptExec} process
 * @returns Nada
 */
function passThroughOutput(process)
{
    copyAll(javaProcess.StdOut, WScript.StdOut);
    copyAll(javaProcess.StdErr, WScript.StdErr);
}

/**
 * fs is assigned an object of type FileSystemObject 
 * @global
 */
var fs = new ActiveXObject("Scripting.FileSystemObject");
/** shell is assigned an object of type WshShell */
var shell = new ActiveXObject("WScript.Shell");
/** env is assigned an object of type WshEnvironment */
var env = shell.Environment("Process");

var argumentString = buildArgumentString(WScript.Arguments);	// returns a String object
var binDir = fs.GetParentFolderName(WScript.ScriptFullName);	// returns a String object
var distDir = fs.GetParentFolderName(binDir);			// returns a String object
var mainDir = fs.GetParentFolderName(distDir);			// returns a String object
var srcDir = fs.GetParentFolderName(mainDir);			// returns a String object
var topDir = fs.GetParentFolderName(srcDir);			// returns a String object
var libDir = fs.BuildPath(distDir, "lib");			// returns a String object
var scriptFile = fs.GetFile(WScript.ScriptFullName);		// returns a File object
var scriptNameWithoutExtension = scriptFile.Name.replace("." + fs.GetExtensionName(scriptFile.Name), "");
var jar = fs.BuildPath(libDir, scriptNameWithoutExtension + "-1.0.jar");

// Find javer
var java = findJava(fs, env);
var outOfMemoryErrorHandler = fs.BuildPath(binDir, "rune-out-of-mem-err-handler.js");
var javerCommand = '"' + java + '" -XX:OnOutOfMemoryError=' + outOfMemoryErrorHandler + ' -jar ' + jar + ' ' + argumentString;

//WScript.Echo("Running: " + javerCommand);
var exitCode;
if (isRunningWithCScript()) {
    var javaProcess = shell.Exec(javerCommand);	// Returns a WshScriptExec object
    passThroughOutput(javaProcess);
    while (javaProcess.Status == 0) {
    	// It's still running, so wait for it.
    	WScript.Sleep(3000);
    }
    exitCode = javaProcess.ExitCode;
} else {
    exitCode = shell.Run(javerCommand, 1, true);
}

WScript.Quit(exitCode);

