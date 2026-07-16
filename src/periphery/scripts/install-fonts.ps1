#####################
#                   #
# install-fonts.ps1 #
#                   #
#####################


# Define command line parameters
param (
    [Parameter(Mandatory = $true)]
    [string]$folderContaingFontsToInstall
)

# Written by Google AI Mode
# Adds all fonts to the active cache and then sends message to apps that fonts have been added.
function Update-FontShell {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory = $true, ValueFromPipeline = $true, ValueFromPipelineByPropertyName = $true)]
        [ValidateScript({ Test-Path $_ -PathType Leaf })]
        [string[]]$FontPath
    )

    begin {
        # Define and load Win32 APIs if not already loaded in the session
        $TypeName = "Win32.FontRefresh"
        if (-not ([System.Management.Automation.PSTypeName]$TypeName).Type) {
            $Win32Signatures = @'
            [DllImport("gdi32.dll", CharSet = CharSet.Unicode)]
            public static extern int AddFontResourceW(string lpFilename);

            [DllImport("user32.dll", CharSet = CharSet.Auto)]
            public static extern IntPtr SendMessageW(IntPtr hWnd, uint Msg, IntPtr wParam, IntPtr lParam);
'@
            Add-Type -MemberDefinition $Win32Signatures -Name "FontRefresh" -Namespace "Win32" | Out-Null
        }
        
        # Track if at least one font was successfully registered to prevent unnecessary broadcast loops
        $NeedsBroadcast = $false
    }

    process {
        # Loop through each item in the array or pipeline item
        foreach ($Path in $FontPath) {
            # Resolve path to absolute format (handles relative paths safely)
            $AbsoluteFontPath = (Get-Item $Path).FullName
            
            Write-Verbose "Injecting font resource into active cache: $AbsoluteFontPath"
            $Result = [Win32.FontRefresh]::AddFontResourceW($AbsoluteFontPath)
            
            if ($Result -gt 0) {
                Write-Output "Successfully registered font: $(Split-Path $AbsoluteFontPath -Leaf)"
                $NeedsBroadcast = $true
            } else {
                Write-Error "Failed to add font resource to system table: $AbsoluteFontPath"
            }
        }
    }

    end {
        # Broadcast the system-wide message exactly ONCE at the very end of processing all fonts
        if ($NeedsBroadcast) {
            Write-Verbose "Broadcasting WM_FONTCHANGE notification to the shell..."
            $HWND_BROADCAST = [IntPtr]0xffff
            $WM_FONTCHANGE = 0x001D
            
            [void]([Win32.FontRefresh]::SendMessageW($HWND_BROADCAST, $WM_FONTCHANGE, [IntPtr]::Zero, [IntPtr]::Zero))
            Write-Verbose "Shell refresh broadcast complete."
        }
    }
}


# Gets system font folder, but this may silently fail because the user doesn't
# have permission to write to this folder. Use the user's font folder instead.
$FONT_NAMESPACE=0x14
$systemFontFolder = (New-Object -ComObject Shell.Application).Namespace($FONT_NAMESPACE)

# Gets the user's font folder.
$localAppDataFolder = [Environment]::GetFolderPath("LocalApplicationData")
$userFontFolder = Join-Path $localAppDataFolder "Microsoft\Windows\Fonts"

# Create the user font folder if it does not exist.
if (-not (Test-Path $userFontFolder)) {
    New-Item -ItemType Directory -Path $userFontFolder -Force
}

# Collect the fonts to install, copy them to the user's font folder, and send the copied files to Update-FontShell.
Get-ChildItem -Path $folderContainingFontsToInstall -Recurse -Include *.ttf, *.otf | ForEach-Object {
    # 1. Execute the copy operation
    $userFontFolder.CopyHere($_.FullName)
    
    # 2. Re-construct the file path inside the target user folder
    $TargetFilePath = Join-Path $userFontFolder.Self.Path $_.Name
    
    write-output "Installing font: $TargetFilePath.FullName"
    # 3. Fetch and output the newly created file object down the pipeline
    Get-Item $TargetFilePath

} | Update-FontShell

