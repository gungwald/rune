package com.alteredmechanism.notepad;

public class SystemPropertyConfigurator {

	public SystemPropertyConfigurator() {
	}
	
	protected static boolean isGnomeDesktop() {
        String sunDesktop = System.getProperty("sun.desktop");
        if (sunDesktop != null && sunDesktop.equalsIgnoreCase("gnome")) {
        	return true;
        }
        else {
        	return false;
        }
	}
	
	public static void autoConfigure() {
        LookAndFeelManager.setSystemLookAndFeel();
        
        // Enable anti-aliased text: http://mindprod.com/jgloss/antialiasing.html#JAVA15
        // This property is used by Java 1.5.
        System.setProperty("swing.aatext", "true");
        // This property is used by Java 1.6. It can be set to:
        //	lcd 	- sub-pixel anti-aliasing
        //	false 	- no anti-aliasing
        //	on		- best for Gnome but not available on Windows
        //	gasp	- Windows standard anti-aliasing
        if (isGnomeDesktop()) {
        	System.setProperty("awt.useSystemAAFontSettings", "on");
        }
        else {
        	System.setProperty("awt.useSystemAAFontSettings", "lcd");
        }
        // Put the main menu at the top on a Mac because that where is should be.
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // Needed for Java 6 on Mac.
        System.setProperty("apple.awt.graphics.UseQuartz", "true");
	}

}
