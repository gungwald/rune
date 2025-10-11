package com.alteredmechanism.javax.swing;

import com.alteredmechanism.rune.Messenger;
import com.alteredmechanism.rune.SystemPropertyConfigurator;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.alteredmechanism.rune.SystemPropertyConfigurator.isGnomeDesktop;
import static com.alteredmechanism.rune.SystemPropertyConfigurator.isMateDesktop;

/**
 * Responsibilities:
 * <ul>
 *     <li>Find "external" LAFs</li>
 *     <li>Add "external" LAFs</li>
 *     <li>Remove external LAFs with duplicate classes</li>
 *     <li>Rename LAFs with duplicate names but different classes</li>
 *     <li>Build user-selectable Swing list menu items</li>
 *     <li>Map LAF/theme combos to user-selectable items</li>
 *     <li>Map selected items to LAF/theme combos</li>
 *     <li>Determine if there are multiple themes available for Metal LAF</li>
 * </ul>
 */
@SuppressWarnings({"UnusedReturnValue"}) // For pseudo-builder pattern
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

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private Messenger messenger;
    private final List<Component> componentsToUpdate = new ArrayList<Component>();
    @SuppressWarnings("unused")
    private final LookAndFeelInfo metalPlaf = null;
    private Class<?> oceanThemeClass;
    private final LookAndFeelIndex lafIndex = LookAndFeelIndex.getInstance();

    /**
     * It needs to be volatile to prevent cache incoherence issues.
     * I don't know what that means, but
     * <a href="https://www.baeldung.com/java-singleton-double-checked-locking">Baeldung</a>
     * says so.
     */
    private static volatile LookAndFeelManager instance;

    /**
     * Double-checked locking implementation of a singleton
     * @return That bitch
     */
    public static LookAndFeelManager getInstance() {
        if (instance == null) {
            synchronized (LookAndFeelManager.class) {
                if (instance == null) {
                    instance = new LookAndFeelManager();
                }
            }
        }
        return instance;
    }

    private LookAndFeelManager() {
        super();
    }

    public LookAndFeelManager setOptimalLookAndFeel() {
        if (System.getProperty("java.specification.version").compareToIgnoreCase("1.4") > 0) {
            setSystemLookAndFeel();
        }
        if (System.getProperty("java.specification.version").compareToIgnoreCase("1.4") >= 0) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
        return this;
    }

    public LookAndFeelManager setSystemLookAndFeel() {
        // Default to the operating system's native look and feel. Duh...
        try {
            String claimedSystemLaf = UIManager.getSystemLookAndFeelClassName();
            String correctSystemLaf;
            logger.log(Level.INFO, "SystemLookAndFeel={0}", claimedSystemLaf);
            // OpenJDK on OpenIndiana incorrectly returns Motif instead of GTK.
            if (!claimedSystemLaf.contains("GTK") && (isMateDesktop() || isGnomeDesktop())) {
                logger.log(Level.WARNING, "Overriding incorrect system look and feel {0} with {1}", new Object[]{claimedSystemLaf, GTK_LAF_CLASS_NAME});
                correctSystemLaf = GTK_LAF_CLASS_NAME;
            } else {
                correctSystemLaf = claimedSystemLaf;
            }
            UIManager.setLookAndFeel(correctSystemLaf);
        } catch (Exception e) {
            messenger.showError(e);
        }
        return this;
    }

    public LookAndFeelManager setMessenger(Messenger messenger) {
        this.messenger = messenger;
        return this;
    }

    public void initChooserMenuItems(JMenu lafMenu) {
        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo laf : lafs) {
            if (laf.getName().equals(METAL_LAF_NAME)) {
                addMenuItemsForMetalThemes(lafMenu, laf);
            } else {
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
            logger.log(Level.WARNING, "Ocean theme for Metal look-and-feel was not found");
            isAvailable = false;
        }
        return isAvailable;
    }

    protected void addMenuItemsForMetalThemes(JMenu lafMenu, LookAndFeelInfo metal) {
        if (isOceanThemeAvailable()) {
            // Add the Ocean theme and the default metal theme.
            // They both have the same laf. The theme will be determined by the name.
            lafIndex.add(METAL_WITH_OCEAN_THEME, metal.getClassName(), metal);
            lafIndex.add(METAL_WITH_STEEL_THEME, metal.getClassName(), metal);
            addMenuItem(METAL_WITH_OCEAN_THEME, lafMenu, metal);
            addMenuItem(METAL_WITH_STEEL_THEME, lafMenu, metal);
        } else {
            // Just add the default Metal theme.
            lafIndex.add(metal.getName(), metal.getClassName(), metal);
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
                logger.log(Level.WARNING, "Failed to get ocean theme", e);
                oceanTheme = getSteelTheme();
            }
        }
        return oceanTheme;
    }

    public void actionPerformed(ActionEvent event) {
        logger.entering(CLASS_NAME, "actionPerformed", event.paramString());
        try {
            // The theme must be set before setting the look and feel to Metal.
            if (METAL_WITH_STEEL_THEME.equals(event.getActionCommand())) {
                MetalLookAndFeel.setCurrentTheme(getSteelTheme());
            } else if (METAL_WITH_OCEAN_THEME.equals(event.getActionCommand())) {
                MetalLookAndFeel.setCurrentTheme(getOceanTheme());
            }
            UIManager.setLookAndFeel((lafIndex.lookupByName(event.getActionCommand())).getClassName());
            // Must be done after setting LAF.
            if (MOTIF_THEME_NAME.equals(event.getActionCommand())) {
                setAllBackgrounds();
            }
            for (Component c : componentsToUpdate) {
                SwingUtilities.updateComponentTreeUI(c);
            }
        } catch (Exception e) {
            messenger.showError(e);
        }
    }

    public void setAllBackgrounds() {
        logger.log(Level.INFO, "Setting Motif Blue");
        Color motifBlue = new ColorUIResource(124, 155, 255);
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key instanceof String) {
                String keyString = (String) key;
                if (keyString.endsWith("background")) {
                    UIManager.put(key, motifBlue);
                }
            }
            logger.info(key + "(" + key.getClass().getName() + ")=" + defaults.get(key));
        }
    }
}
