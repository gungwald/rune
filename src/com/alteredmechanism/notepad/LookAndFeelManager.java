package com.alteredmechanism.notepad;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


public class LookAndFeelManager {
    
    protected ButtonGroup buttonGroup = new ButtonGroup();
    private Messenger messenger;
    
    public static void setSystemLookAndFeel() {
        // Default to the operating system's native look and feel. Duh...
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
        	new Messenger(LookAndFeelManager.class.getName()).showError(e);
        }
    }

    public LookAndFeelManager(Messenger messenger) {
        super();
        this.messenger = messenger;
    }

    public void initChooserMenuItems(JMenu lookAndFeelMenu, Component[] componentsToUpdate) {
        installOpenLookLookAndFeel();
        LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        Map lookMap = new HashMap();
        LookAndFeelActionListener lookListener = new LookAndFeelActionListener(lookMap, componentsToUpdate, messenger);
        for (int i = 0; i < looks.length; i++) {
            LookAndFeelInfo look = looks[i];
            lookMap.put(look.getName(), look);
            JRadioButtonMenuItem lookMenuItem = new JRadioButtonMenuItem(look.getName());
            lookMenuItem.addActionListener(lookListener);
            buttonGroup.add(lookMenuItem);
            lookAndFeelMenu.add(lookMenuItem);
            if (look.getName().equals(UIManager.getLookAndFeel().getName())) {
            	lookMenuItem.setSelected(true);
            }
        }
    }

    /**
     * Installs the Open Look look and feel, if it can be found in the 
     * class path. Otherwise, it does nothing.
     */
    protected void installOpenLookLookAndFeel() {
        try {
            Class.forName("net.sourceforge.openlook_plaf.OpenLookLookAndFeel");
            UIManager.installLookAndFeel("Open Look", "net.sourceforge.openlook_plaf.OpenLookLookAndFeel");
        }
        catch (ClassNotFoundException e) {
            // If Open Look is not available, just do nothing.
        }
    }

}
