package com.alteredmechanism.notepad;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;


public class LookAndFeelManager implements ActionListener {
	
	public static final String OCEAN_THEME_CLASS_NAME = "javax.swing.plaf.metal.OceanTheme";
	public static final String DEFAULT_METAL_THEME_CLASS_NAME = "javax.swing.plaf.metal.DefaultMetalTheme";
	public static final String METAL_STEEL_THEME_ID = "Metal with Steel Theme";
	public static final String METAL_OCEAN_THEME_ID = "Metal";
	public static final String MOTIF_THEME_ID = "CDE/Motif";


    protected ButtonGroup buttonGroup = new ButtonGroup();
    private Messenger messenger;
    private Map lookMap = new TreeMap();
    private List componentsToUpdate = new ArrayList();
    protected LookAndFeelInfo metalPlaf = null;
    protected Class oceanThemeClass = null;
    
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
            if (look.getName().equals("Metal")) {
            	metalPlaf = look;
            }
            lookMap.put(look.getName(), look);
            JRadioButtonMenuItem lookMenuItem = new JRadioButtonMenuItem(look.getName());
            lookMenuItem.addActionListener(this);
            buttonGroup.add(lookMenuItem);
            lookAndFeelMenu.add(lookMenuItem);
            if (look.getName().equals(UIManager.getLookAndFeel().getName())) {
            	lookMenuItem.setSelected(true);
            }
        }
        try {
			oceanThemeClass = Class.forName(OCEAN_THEME_CLASS_NAME);
			Class.forName(DEFAULT_METAL_THEME_CLASS_NAME);
			String lookName = METAL_STEEL_THEME_ID;
			lookMap.put(lookName, metalPlaf);
			JRadioButtonMenuItem lookMenuItem = new JRadioButtonMenuItem(lookName);
            lookMenuItem.addActionListener(this);
            buttonGroup.add(lookMenuItem);
            lookAndFeelMenu.add(lookMenuItem);
		} catch (ClassNotFoundException e) {
			System.err.println("No steel theme");
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
        	// The theme must be set before setting the look and feel
        	//  to Metal.
        	if (METAL_STEEL_THEME_ID.equals(event.getActionCommand())) {
        		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        	}
        	else if (METAL_OCEAN_THEME_ID.equals(event.getActionCommand())) {
        		MetalTheme oceanThemeInstance = (MetalTheme) oceanThemeClass.newInstance();
        		MetalLookAndFeel.setCurrentTheme(oceanThemeInstance);        		
        	}
            UIManager.setLookAndFeel(((LookAndFeelInfo)lookMap.get(event.getActionCommand())).getClassName());
        	// Must be done after setting LAF.
            if (MOTIF_THEME_ID.equals(event.getActionCommand())) {
            	setAllBackgrounds();
        	}
            for (int i = 0; i < componentsToUpdate.size(); i++) {
                Component c = (Component) componentsToUpdate.get(i);
                SwingUtilities.updateComponentTreeUI(c);
            }
        }
        catch (Exception e) {
        	messenger.showError(e);
        }
    }

	public void setAllBackgrounds() {
       	System.out.println("Setting Motif Blue");
       	Color motifBlue = new ColorUIResource(124, 155, 255);
		UIDefaults defaults = UIManager.getDefaults();
		Enumeration keys = defaults.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			if (key instanceof String) {
				String keyString = (String) key;
				if (keyString.endsWith("background")) {
					UIManager.put(key, motifBlue);
				}
			}
			System.out.println(key + "(" + key.getClass().getName() + ")=" + defaults.get(key));
		}
	}
}
