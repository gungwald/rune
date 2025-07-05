package com.alteredmechanism.javax.swing.plaf;

import javax.swing.*;

public class LookAndFeelWithTheme extends UIManager.LookAndFeelInfo {
    private String themeName;
    private String themeClassName;

    /**
     * Constructs a <code>UIManager</code>s
     * <code>LookAndFeelInfo</code> object.
     *
     * @param lafName      a <code>String</code> specifying the name of
     *                  the look and feel
     * @param lafClassName a <code>String</code> specifiying the name of
     *                  the class that implements the look and feel
     */
    public LookAndFeelWithTheme(String lafName, String lafClassName) {
        super(lafName, lafClassName);
        themeName = null;
        themeClassName = null;
    }

    public LookAndFeelWithTheme(String lafName, String lafClassName, String themeName, String themeClassName) {
        super(lafName, lafClassName);
        this.themeName = themeName;
        this.themeClassName = themeClassName;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getThemeClassName() {
        return themeClassName;
    }
}
