package com.alteredmechanism.izpack;

import com.alteredmechanism.rune.Executor;
import com.izforge.izpack.event.SimpleInstallerListener;
import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.util.AbstractUIProgressHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class XdgDesktopMenu extends SimpleInstallerListener {
    @Override
    public void afterPacks(AutomatedInstallData automatedInstallData, AbstractUIProgressHandler abstractUIProgressHandler) throws Exception {
        super.afterPacks(automatedInstallData, abstractUIProgressHandler);
        System.out.printf("Install variables: %s%n", automatedInstallData.getVariables().toString());
        File startScript = new File(automatedInstallData.getVariable("INSTALL_PATH") + "/bin/rune");
        File tmpFile = makeTempDesktopFile(startScript);
        Executor.exec(new String[] {"xdg-desktop-menu", "install", tmpFile.getAbsolutePath()});
    }

    private File makeTempDesktopFile(File startScript) throws IOException {
        File tmp = File.createTempFile("rune", ".desktop");
        PrintWriter out = new PrintWriter(tmp);
        out.println("[Desktop Entry]");
        out.println("Name=Rune");
        out.printf("Exec=%s", startScript.getAbsolutePath());
        out.println("Type=Application");
        out.println("StartupNotify=true");
        out.println("Terminal=false");
        out.println("Comment=Apple IIgs Emulator");
        out.println("Path=$HOME");
        out.println("Categories=Emulator");
        out.println("Encoding=UTF-8");
        out.println("Icon=$ICON_NAME");
        out.println("#");
        out.println("# See here for a list of valid categories:");
        out.println("# https://specifications.freedesktop.org/menu-spec/latest/apa.html");
        out.close();
        return tmp;
    }

}
