package com.alteredmechanism.izpack;

import com.alteredmechanism.rune.Executor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class XdgDesktopMenu {

    public void createShortcuts(File installPath) throws IOException, InterruptedException, InvalidInstallPathException {
        if (installPath.isDirectory()) {
            File startScript = new File(installPath, "/bin/rune");
            File icon = new File(installPath, "share/icons/vegvisir.png");
            File tmpFile = makeTempDesktopFile(startScript, icon);
            Executor.exec(new String[]{"xdg-desktop-menu", "install", tmpFile.getAbsolutePath()});
        } else {
            throw new InvalidInstallPathException("Path missing or not directory: " + installPath.getAbsolutePath());
        }
    }

    private File makeTempDesktopFile(File startScript, File icon) throws IOException {
        File tmp = File.createTempFile("altered.mechanism-rune-", ".desktop");
        PrintWriter out = new PrintWriter(tmp);
        out.println("[Desktop Entry]");
        out.println("Name=Rune");
        out.printf("Exec=%s%n", startScript.getAbsolutePath());
        out.println("Type=Application");
        out.println("StartupNotify=true");
        out.println("Terminal=false");
        out.println("Comment=A text editor that works like you'd expect");
        out.println("Path=~");
        out.println("Categories=Utility");
        out.println("Encoding=UTF-8");
        out.printf("Icon=%s%n", icon.getAbsolutePath());
        out.println("#");
        out.println("# See here for a list of valid categories:");
        out.println("# https://specifications.freedesktop.org/menu-spec/latest/apa.html");
        out.close();
        return tmp;
    }

    public File determineInstallPath() throws InvalidInstallPathException {
        File installPath;
        File javaClassPath = new File(System.getProperty("java.class.path"));
        File parentDir = javaClassPath.getParentFile();
        if (parentDir.getName().equals("java")) {
            File grandparentDir = parentDir.getParentFile();
            if (grandparentDir.getName().equals("share")) {
                installPath = grandparentDir.getParentFile();
            } else {
                throw new InvalidInstallPathException("Install path cannot be automatically determined");
            }
        } else if (parentDir.getName().equals("lib")) {
            installPath = parentDir.getParentFile();
        } else {
            throw new InvalidInstallPathException("Install path cannot be automatically determined");
        }
        return installPath;
    }

    public static void main(String[] args) {
        try {
            XdgDesktopMenu shortcutCreator = new XdgDesktopMenu();
            File installPath;
            if (args.length > 0) {
                installPath = new File(args[0]);
            } else {
                installPath = shortcutCreator.determineInstallPath();
            }
            shortcutCreator.createShortcuts(installPath);
        } catch (InvalidInstallPathException e) {
            e.printStackTrace();
            System.err.println("XdgDesktopMenu: A valid, existing install path should be passed on the command line");
            System.err.println("XdgDesktopMenu: For example: java -jar xdg-desktop-menu.jar /home/fred/opt/rune");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
