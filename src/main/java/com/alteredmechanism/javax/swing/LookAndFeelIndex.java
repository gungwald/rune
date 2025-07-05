package com.alteredmechanism.javax.swing;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Needed to dedup LAFs and find the LAFs selected by the user in the UI. Contains
 * built-in and external LAFs that get installed.
 */
class LookAndFeelIndex {
    private static final String CLASS_NAME = LookAndFeelIndex.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public static final String[] EXTERNAL_PLAFS = {
            "com.incors.plaf.kunststoff.KunststoffLookAndFeel",
            "com.birosoft.liquid.LiquidLookAndFeel",
            "com.jgoodies.looks.plastic.Plastic3DLookAndFeel",
            "com.jgoodies.looks.plastic.PlasticLookAndFeel",
            "com.jgoodies.looks.plastic.PlasticXPLookAndFeel",
            "com.jgoodies.looks.windows.WindowsLookAndFeel",
            "net.sourceforge.mlf.metouia.MetouiaLookAndFeel",
            "org.jvnet.substance.SubstanceLookAndFeel",
            "net.sourceforge.openlook_plaf.OpenLookLookAndFeel"
    };

    private final Map<String, LookAndFeelInfo> nameLookup = new HashMap<String, LookAndFeelInfo>();
    private final Map<String, LookAndFeelInfo> classNameLookup = new HashMap<String, LookAndFeelInfo>();
    private final Map<String, LookAndFeelInfo> uiNameLookup = new HashMap<String, LookAndFeelInfo>();

    // ***********************************************************************
    //                             Singleton
    // ***********************************************************************

    /**
     * The only instance of this class.
     * It needs to be volatile to prevent cache incoherence issues.
     * I don't know what that means, but
     * <a href="https://www.baeldung.com/java-singleton-double-checked-locking">Baeldung</a>
     * says so.
     */
    private static volatile LookAndFeelIndex instance;
    /**
     * Double-checked locking implementation of a singleton method to
     * retrieve the single instance.
     * @return The singleton
     */
    public static LookAndFeelIndex getInstance() {
        if (instance == null) {
            synchronized (LookAndFeelIndex.class) {
                if (instance == null) {
                    instance = new LookAndFeelIndex();
                }
            }
        }
        return instance;
    }
    /**
     * A singleton constructor must be private.
     */
    private LookAndFeelIndex() {
        super();
        installExternalLafs();
    }
    // ***********************************************************************
    //                     End of Singleton making code
    // ***********************************************************************

    protected void add(LookAndFeelInfo[] lafis) {
        for (LookAndFeelInfo lafi : lafis) {
            add(lafi);
        }
    }

    protected void add(LookAndFeelInfo i) {
        if (nameLookup.containsKey(i.getName())) {
            logger.log(Level.WARNING, "Duplicated LAF name: " + i.getName());
        } else if (classNameLookup.containsKey(i.getClassName())) {
            logger.log(Level.WARNING, "Duplicated LAF class name: " + i.getClassName());
        } else {
            nameLookup.put(i.getName(), i);
            classNameLookup.put(i.getClassName(), i);
        }
    }

    protected void add(String name, String className, LookAndFeelInfo l) {
        nameLookup.put(name, l);
        classNameLookup.put(className, l);
    }

    protected LookAndFeelInfo lookupByName(String name) {
        return nameLookup.get(name);
    }

    protected LookAndFeelInfo lookupByClassName(String className) {
        return classNameLookup.get(className);
    }
    public LookAndFeel lookupLafFromClassName(String lafClassName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // The target runtime is Java 5, so don't try to "fix" this.
        final Class<?> lafClazz = Class.forName(lafClassName);
        final Constructor<?> cons = lafClazz.getConstructor();
        return (LookAndFeel) cons.newInstance();
        // The target runtime is Java 5, so don't try to "fix" this.
    }
    /**
     * Installs additional look and feels, if it can be found in the
     * class path. Otherwise, it does nothing.
     */
    public void installExternalLafs() {
        for (String lafClassName : EXTERNAL_PLAFS) {
            try {
                LookAndFeel laf = lookupLafFromClassName(lafClassName);
                installIfNotDuplicate(laf);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "Failed to install look & feel. Check if class version is greater than Java 5.0: " + e);
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Failed to install look & feel: " + t);
            }
        }
    }

    private void installIfNotDuplicate(LookAndFeel laf) {
        String lafName = laf.getName();
        String lafClassName = laf.getClass().getName();
        String finalLafName;
        if (this.lookupByClassName(lafClassName) == null) {
            logger.log(Level.WARNING, "Blocking install of already existing look and feel class {0} {1}", new String[]{lafName, lafClassName});
        } else {
            if (this.lookupByName(lafName) == null) {
                finalLafName = lafName + " " + new Random(new Date().getTime());
                logger.log(Level.WARNING, "Found duplicate LAF name with different class names: {0}", lafName);
            } else {
                finalLafName = lafName;
            }
            logger.log(Level.FINE, "Trying {0} PLAF with class {1}", new String[]{finalLafName, lafClassName});
            UIManager.installLookAndFeel(finalLafName, lafClassName);
        }
    }

}
