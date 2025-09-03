package com.alteredmechanism.rune;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration extends PropertiesFileDataStore {

	public static final File HOME = new File(System.getProperty("user.home"));
	public static final File CONFIG_DIR = new File(HOME, ".rune");
	public static final File CONFIG_FILE = new File(CONFIG_DIR, "config.properties");

	private final Properties props = new Properties();

	private boolean replaceTabsWithSpaces = true;
	private int replacedTabWidth = 4;
	private int displayedTabWidth = 4;
	private List<File> openFiles = null;

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

	public boolean isReplaceTabsWithSpacesEnabled() {
		if (replaceTabsWithSpaces == null) {
			replaceTabsWithSpaces = getBoolean("replace.tabs.with.spaces");
		}
		return replaceTabsWithSpaces.booleanValue();
	}

	public Integer getDisplayedTabWidth() {
		return displayedTabWidth;
	}

	public void setDisplayedTabWidth(Integer displayedTabWidth) {
		this.displayedTabWidth = displayedTabWidth;
	}

	public List<File> getOpenFiles() {
		return getFileList("open.files");
	}

	public void setOpenFiles(List<File> openFiles) {
		setFileList("open.files", openFiles);
	}

	public Integer getReplacedTabWidth() {
		return replacedTabWidth;
	}

	public void setReplacedTabWidth(Integer replacedTabWidth) {
		this.replacedTabWidth = replacedTabWidth;
	}
}
