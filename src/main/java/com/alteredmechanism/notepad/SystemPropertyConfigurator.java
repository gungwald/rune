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

	public static boolean isMateDesktop() {
		return System.getenv("XDG_CURRENT_DESKTOP").equalsIgnoreCase("mate");
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
			// https://www.oracle.com/technical-resources/articles/javase/javatomac.html
			System.out.println("Detected Mac OS");

			// Java 1.3
			System.setProperty("com.apple.macos.useScreenMenuBar", "true"); // Default is false
			System.setProperty("com.apple.macos.smallTabs", "true"); // Default is false
			System.setProperty("com.apple.hwaccel", "true"); // only needed for 1.3.1 on OS X 10.2
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", Rune.USER_FACING_APP_NAME);
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false"); // Default is true
			System.setProperty("com.apple.mrj.application.live-resize", "true"); // Default is false

			// Java 1.4
			// Put the main menu at the top on a Mac because that where is should be.
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("apple.awt.showGrowBox", "true");

			// Java 1.5
			// TBD...
			
			// Java 6
			System.setProperty("apple.awt.graphics.UseQuartz", "true");
			
			// For unknown Java version
			System.setProperty("apple.awt.brushMetalLook", "true");
			System.setProperty("apple.awt.textantialiasing", "true");
			// ref: http://developer.apple.com/releasenotes/Java/Java142RNTiger/1_NewFeatures/chapter_2_section_3.html
			System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
		}

		// The above system properties should be set first.
		LookAndFeelManager.setOptimalLookAndFeel();
	}

}
