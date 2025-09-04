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

	private Boolean replaceTabsWithSpaces = null;
	private Integer replacedTabWidth = null;
	private Integer displayedTabWidth = null;
	private List<File> openFiles = null;

	private static Configuration singleton = null;

	public static Configuration getInstance() throws IOException, FileCreationException {
		if (singleton == null) {
			singleton = new Configuration();
		}
		return singleton;
	}

	private Configuration() throws IOException, FileCreationException {
		super(CONFIG_FILE);
	}

	public boolean isReplaceTabsWithSpacesEnabled() {
		if (replaceTabsWithSpaces == null) {
			replaceTabsWithSpaces = getBoolean("replace.tabs.with.spaces");
		}
		return replaceTabsWithSpaces;
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
