package com.alteredmechanism.rune;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Configuration extends PropertiesFileDataStore {

    protected static final String REPLACE_TABS_WITH_SPACES_KEY = "replace.tabs.with.spaces";
    protected static final String DISPLAYED_TAB_WIDTH_KEY = "displayed.tab.width";
    protected static final String REPLACED_TAB_WIDTH_KEY = "replaced.tab.width";
	protected static final String LIST_OF_OPEN_FILES_KEY = "list.of.open.files";

	/**
	 * It is required to provide a default value for each property in the configuration.
	 * This avoids a bunch of code to manage missing properties.
	 */
    protected static final Properties defaults = new Properties() {{
        put(REPLACE_TABS_WITH_SPACES_KEY, "true");
        put(DISPLAYED_TAB_WIDTH_KEY, "4");
        put(REPLACED_TAB_WIDTH_KEY, "4");
		put(LIST_OF_OPEN_FILES_KEY, "");
    }};

	public static final File home = new File(System.getProperty("user.home"));
	public static final File configDir = new File(home, ".rune");
	public static final File configFile = new File(configDir, "config.properties");

	// **************************** Singleton setup **********************************
	private static Configuration singleton = null;
	/** The static method to retrieve the one and only instance. */
	public static Configuration getInstance() throws IOException, FileCreationException {
		if (singleton == null) {
			singleton = new Configuration();
		}
		return singleton;
	}
	/** A singleton requires a private constructor. */
	private Configuration() throws IOException, FileCreationException {
		super(configFile);
	}
	// **************************** Singleton setup **********************************

	public boolean isReplaceTabsWithSpacesSet() {
		return getBoolean(REPLACE_TABS_WITH_SPACES_KEY);
	}

    private void setReplaceTabsWithSpaces(boolean value) {
        setBoolean(REPLACE_TABS_WITH_SPACES_KEY, value);
    }

    public int getDisplayedTabWidth() {
		return getInteger(DISPLAYED_TAB_WIDTH_KEY);
	}

	public void setDisplayedTabWidth(int value) {
		setInteger(DISPLAYED_TAB_WIDTH_KEY, value);
	}

	public List<File> getListOfOpenFiles() {
		return getFileList(LIST_OF_OPEN_FILES_KEY);
	}

	public void setListOfOpenFiles(List<File> openFiles) {
		setFileList(LIST_OF_OPEN_FILES_KEY, openFiles);
	}

	public int getReplacedTabWidth() {
		return getInteger(REPLACED_TAB_WIDTH_KEY);
	}

	public void setReplacedTabWidth(int value) {
		setInteger(REPLACED_TAB_WIDTH_KEY, value);
	}
}
