package com.alteredmechanism.rune;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final String CLASS_NAME = LookAndFeelManager.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static final String OCEAN_THEME_CLASS_NAME = "javax.swing.plaf.metal.OceanTheme";
    public static final String DEFAULT_METAL_THEME_CLASS_NAME = "javax.swing.plaf.metal.DefaultMetalTheme";
    public static final String METAL_WITH_STEEL_THEME = "Metal with Steel Theme";
    public static final String METAL_WITH_OCEAN_THEME = "Metal with Ocean Theme";
    public static final String METAL_LAF_NAME = "Metal";
    public static final String MOTIF_THEME_NAME = "CDE/Motif";
    public static final String GTK_LAF_CLASS_NAME = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

    protected ButtonGroup buttonGroup = new ButtonGroup();
    private final Messenger messenger;
    private final Map<String, LookAndFeelInfo> lookMap = new TreeMap<String, LookAndFeelInfo>();
    private final List<Component> componentsToUpdate = new ArrayList<Component>();
    protected LookAndFeelInfo metalPlaf = null;
    private Class<?> oceanThemeClass;

    public static void setSystemLookAndFeel() {
        // Default to the operating system's native look and feel. Duh...
        try {
            String systemLafClassName = UIManager.getSystemLookAndFeelClassName();
            logger.log(Level.INFO, "SystemLookAndFeel={0}", systemLafClassName);
            // OpenJDK on OpenIndiana Mate desktop incorrectly returns Motif instead of GTK.
            if (systemLafClassName.indexOf("GTK") == -1 && SystemPropertyConfigurator.isMateDesktop()) {
                logger.log(Level.WARNING, "Overriding system look and feel {0} with {1} for Mate desktop",
                        new Object[]{systemLafClassName, GTK_LAF_CLASS_NAME});
                systemLafClassName = GTK_LAF_CLASS_NAME;
            }
            if (systemLafClassName.indexOf("GTK") == -1 && SystemPropertyConfigurator.isGnomeDesktop()) {
                logger.log(Level.WARNING, "Overriding system look and feel {0} with {1} for Gnome desktop",
                        new Object[] {systemLafClassName, GTK_LAF_CLASS_NAME});
                systemLafClassName = GTK_LAF_CLASS_NAME;
            }
            UIManager.setLookAndFeel(systemLafClassName);
        } catch (Exception e) {
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

    public void initChooserMenuItems(JMenu lafMenu) {
        installOpenLookLookAndFeel();
        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo laf : lafs) {
            if (laf.getName().equals(METAL_LAF_NAME)) {
                addMenuItemsForMetalThemes(lafMenu, laf);
            } else {
                lookMap.put(laf.getName(), laf);
                addMenuItem(lafMenu, laf);
            }
        }
    }

    protected boolean isOceanThemeAvailable() {
        boolean isAvailable;
        try {
            oceanThemeClass = Class.forName(OCEAN_THEME_CLASS_NAME);
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            System.out.println("Ocean theme for Metal look-and-feel was not found");
            isAvailable = false;
        }
        return isAvailable;
    }

    protected void addMenuItemsForMetalThemes(JMenu lafMenu, LookAndFeelInfo metal) {
        if (isOceanThemeAvailable()) {
            // Add the Ocean theme and the default metal theme.
            // They both have the same laf. The theme will be determined by the name.
            lookMap.put(METAL_WITH_OCEAN_THEME, metal);
            lookMap.put(METAL_WITH_STEEL_THEME, metal);
            addMenuItem(METAL_WITH_OCEAN_THEME, lafMenu, metal);
            addMenuItem(METAL_WITH_STEEL_THEME, lafMenu, metal);
        } else {
            // Just add the default Metal theme.
            lookMap.put(metal.getName(), metal);
            addMenuItem(lafMenu, metal);
        }
    }

    protected void addMenuItem(JMenu lafMenu, LookAndFeelInfo laf) {
        addMenuItem(laf.getName(), lafMenu, laf);
    }

    protected void addMenuItem(String name, JMenu lafMenu, LookAndFeelInfo look) {
        JRadioButtonMenuItem lookMenuItem = new JRadioButtonMenuItem(name);
        lookMenuItem.addActionListener(this);
        buttonGroup.add(lookMenuItem);
        lafMenu.add(lookMenuItem);
        if (look.getName().equals(UIManager.getLookAndFeel().getName())) {
            lookMenuItem.setSelected(true);
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
        } catch (ClassNotFoundException e) {
            // If Open Look is not available, just do nothing.
        }
    }

    private MetalTheme steelTheme = null;
    protected MetalTheme getSteelTheme() {
        if (steelTheme == null) {
            steelTheme = new DefaultMetalTheme();
        }
        return steelTheme;
    }

    private MetalTheme oceanTheme = null;
    protected MetalTheme getOceanTheme() {
        if (oceanTheme == null) {
            try {
                oceanTheme = (MetalTheme) oceanThemeClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                oceanTheme = getSteelTheme();
            }
        }
        return oceanTheme;
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.paramString());
        try {
            // The theme must be set before setting the look and feel to Metal.
            if (METAL_WITH_STEEL_THEME.equals(event.getActionCommand())) {
                MetalLookAndFeel.setCurrentTheme(getSteelTheme());
            } else if (METAL_WITH_OCEAN_THEME.equals(event.getActionCommand())) {
                MetalLookAndFeel.setCurrentTheme(getOceanTheme());
            }
            UIManager.setLookAndFeel((lookMap.get(event.getActionCommand())).getClassName());
            // Must be done after setting LAF.
//            if (MOTIF_THEME_NAME.equals(event.getActionCommand())) {
//                setAllBackgrounds();
//            }
            for (Component c : componentsToUpdate) {
                SwingUtilities.updateComponentTreeUI(c);
            }
        } catch (Exception e) {
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
