package com.alteredmechanism.izpack;

import com.izforge.izpack.event.SimpleInstallerListener;
import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.util.AbstractUIProgressHandler;

public class XdgDesktopMenu extends SimpleInstallerListener {
    @Override
    public void afterPacks(AutomatedInstallData automatedInstallData, AbstractUIProgressHandler abstractUIProgressHandler) throws Exception {
        super.afterPacks(automatedInstallData, abstractUIProgressHandler);
        System.out.printf("Install variables: %s", automatedInstallData.getVariables().toString());
    }
}
