package com.alteredmechanism.notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Configuration {

	public static final File HOME = new File(System.getProperty("user.home"));
	public static final File CONFIG_DIR = new File(HOME, ".hreodwrit");
	public static final File CONFIG_FILE = new File(CONFIG_DIR, "config.properties");

	private Properties props = new Properties();

	private boolean replaceTabsWithSpaces;
	private int replacedTabWidth = 4;
	private int displayedTabWidth = 8;
	private List openFiles;

	private static Configuration singleton = null;

	public static Configuration getInstance() throws IOException {
		if (singleton == null) {
			singleton = new Configuration();
		}
		return singleton;
	}

	private Configuration() throws IOException {
		load();
	}

	private void load() throws IOException {
		if (CONFIG_FILE.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(CONFIG_FILE);
				props.load(input);
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void save() throws IOException {
		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdir();
		}
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(CONFIG_FILE);
			props.store(output, "blah");
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
    
	public boolean isReplaceTabsWithSpacesEnabled() {
		return false;
	}
}
