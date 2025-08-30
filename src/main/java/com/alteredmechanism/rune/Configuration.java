package com.alteredmechanism.rune;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {

	public static final File HOME = new File(System.getProperty("user.home"));
	public static final File CONFIG_DIR = new File(HOME, ".rune");
	public static final File CONFIG_FILE = new File(CONFIG_DIR, "config.properties");

	private final Properties props = new Properties();

	private Boolean replaceTabsWithSpaces = null;
	private Integer replacedTabWidth = null;
	private Integer displayedTabWidth = null;
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

	private void load() throws IOException {
		if (CONFIG_FILE.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(CONFIG_FILE);
				props.load(input);
                openFiles = csvToFileList(props.getProperty("open.files"));
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

    protected List<File> csvToFileList(String csv) {
        List<File> files = new ArrayList<File>();
        for (String name : csv.split(":")) {
            files.add(new File(name));
        }
        return files;
    }

    public void save() throws IOException {
		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdir();
		}
        props.setProperty("open.files", fileListToCsv(openFiles));
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(CONFIG_FILE);
			props.store(output, "Auto-generated from last editor state");
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

    protected String fileListToCsv(List<File> files) {
        StringBuilder csv = new StringBuilder();
        for (File file : files) {
            csv.append(file.getAbsolutePath());
            csv.append(':');
        }
        if (csv.length() > 0) {
            csv.setLength(csv.length() - 1); // Remove trailing colon
        }
        return csv.toString();
    }
    
	public Boolean getBoolean(String name) {
		String value = props.getProperty(name);
		if (value == null) {
			System.err.println("Missing property: " + name);
		}
		else {
			System.out.println("Loaded property: " + name + "=" + value);
		}
		return Boolean.valueOf(value);
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
		return openFiles;
	}

	public void setOpenFiles(List<File> openFiles) {
		this.openFiles = openFiles;
	}

	public Integer getReplacedTabWidth() {
		return replacedTabWidth;
	}

	public void setReplacedTabWidth(Integer replacedTabWidth) {
		this.replacedTabWidth = replacedTabWidth;
	}
}
