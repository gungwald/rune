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
		// Enable anti-aliased text: http://mindprod.com/jgloss/antialiasing.html#JAVA15
		// This property is used by Java 1.5.
		System.setProperty("swing.aatext", "true");
		
		// This property is used by Java 1.6. It can be set to:
		// lcd - sub-pixel anti-aliasing
		// false - no anti-aliasing
		// on - best for Gnome but not available on Windows
		// gasp - Windows standard anti-aliasing
//		if (isGnomeDesktop()) {
//			System.out.println("Detected Gnome desktop");
//			System.setProperty("awt.useSystemAAFontSettings", "on");
//		}
//		else {
//			System.setProperty("awt.useSystemAAFontSettings", "lcd");
//		}

		// Mac OS
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			System.out.println("Detected Mac OS");

			// Java 1.3
			System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "true");
			System.setProperty("com.apple.hwaccel", "true"); // only needed for 1.3.1 on OS X 10.2
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Visitour");

			// Java 1.4
			// Put the main menu at the top on a Mac because that where is should be.
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("apple.awt.showGrowBox", "true");

			// Java 1.5
			// TBD...
			
			// Java 6
			System.setProperty("apple.awt.graphics.UseQuartz", "true");
		}

		// The above system properties should be set first.
		LookAndFeelManager.setOptimalLookAndFeel();
	}

}
