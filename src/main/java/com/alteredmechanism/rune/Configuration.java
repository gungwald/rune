package com.alteredmechanism.rune;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Configuration extends PropertiesFileDataStore {

    protected static final String REPLACE_TABS_WITH_SPACES_KEY = "replace.tabs.with.spaces";
    protected static final String DISPLAYED_TAB_WIDTH_KEY = "displayed.tab.width";
    protected static final String REPLACED_TAB_WIDTH_KEY = "replaced.tab.width";

	public static final File home = new File(System.getProperty("user.home"));
	public static final File configDir = new File(home, ".rune");
	public static final File configFile = new File(configDir, "config.properties");

    protected static final Properties defaults = new Properties() {{
        put(REPLACE_TABS_WITH_SPACES_KEY, "true");
        put(DISPLAYED_TAB_WIDTH_KEY, "4");
        put(REPLACED_TAB_WIDTH_KEY, "4");
    }};

    private static Configuration singleton = null;

	public static Configuration getInstance() throws IOException, FileCreationException {
		if (singleton == null) {
			singleton = new Configuration();
		}
		return singleton;
	}

	private Configuration() throws IOException, FileCreationException {
		super(configFile);
	}

	public boolean isReplaceTabsWithSpacesSet() {
		Boolean storedValue = getBoolean(REPLACE_TABS_WITH_SPACES_KEY);
        boolean valueToReturn;
        if (storedValue == null) {
            valueToReturn = defaultValue;
            setReplaceTabsWithSpaces(defaultValue);
        } else {
            valueToReturn = storedValue;
        }
        return valueToReturn;
	}

    private void setReplaceTabsWithSpaces(boolean value) {
        setBoolean(REPLACE_TABS_WITH_SPACES_KEY, value);
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
