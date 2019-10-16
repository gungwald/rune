package com.alteredmechanism.notepad;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


public class LookAndFeelManager implements ActionListener {
    
    protected ButtonGroup buttonGroup = new ButtonGroup();
    private Messenger messenger;
    private Map lookMap = new TreeMap();
    private List componentsToUpdate = new ArrayList();
    
    public static void setSystemLookAndFeel() {
        // Default to the operating system's native look and feel. Duh...
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
        	new Messenger(LookAndFeelManager.class.getName()).showError(e);
        }
    }
    
    public static void setOptimalLookAndFeel() {
        if (System.getProperty("java.specification.version").compareToIgnoreCase("1.4") > 0) {
            setSystemLookAndFeel();
        }
    }

    public LookAndFeelManager(Messenger messenger) {
        super();
        this.messenger = messenger;
    }

    public void initChooserMenuItems(JMenu lookAndFeelMenu) {
        installOpenLookLookAndFeel();
        LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < looks.length; i++) {
            LookAndFeelInfo look = looks[i];
            lookMap.put(look.getName(), look);
            JRadioButtonMenuItem lookMenuItem = new JRadioButtonMenuItem(look.getName());
            lookMenuItem.addActionListener(this);
            buttonGroup.add(lookMenuItem);
            lookAndFeelMenu.add(lookMenuItem);
            if (look.getName().equals(UIManager.getLookAndFeel().getName())) {
            	lookMenuItem.setSelected(true);
            }
        }
    }
    
    public void addComponentToUpdate(Component c) {
    	componentsToUpdate.add(c);
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

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.paramString());
        try {
            UIManager.setLookAndFeel(((LookAndFeelInfo)lookMap.get(event.getActionCommand())).getClassName());
            for (int i = 0; i < componentsToUpdate.size(); i++) {
                Component c = (Component) componentsToUpdate.get(i);
                SwingUtilities.updateComponentTreeUI(c);
//                if (c instanceof Window) {
//                    ((Window) c).pack();
//                }
            }
        }
        catch (Exception e) {
        	messenger.showError(e);
        }
    }
}
