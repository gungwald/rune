package com.alteredmechanism.rune;

import com.alteredmechanism.rune.actions.SaveAction;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesFileDataStore extends Properties {

    private static final String CLASS_NAME = PropertiesFileDataStore.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    protected File file;

    public PropertiesFileDataStore(File f) throws IOException, FileCreationException {
        file = f;
        File configDir = file.getParentFile();
        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                throw new FileCreationException("Failed to create directory", configDir);
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new FileCreationException("Failed to create file", file);
            }
        }
        load();
    }

    /**
     * Must remain a private method because it is called from the constructor.
     * @throws IOException If something bad happens
     */
    private void load() throws IOException {
        FileInputStream input = new FileInputStream(file);
        try {
            load(input);
        } finally {
            close(input, file);
        }
    }

    public void save() throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            store(output, "Auto-generated from last editor state");
        } finally {
            close(output, file);
        }
    }

    /**
     * The list of files from the file store.
     * @param key The name of the identifier of the file list
     * @return The list of files or null if it does not exist
     */
    public List<File> getFileList(String key) {
        String value = getProperty(key);
        List<File> files;
        if (value == null) {
            files = null;
        } else {
            files = new ArrayList<File>();
            for (String name : value.split(":")) {
                files.add(new File(name));
            }
        }
        return files;
    }

    protected void setFileList(String key, List<File> files) {
        StringBuilder value = new StringBuilder();
        for (File file : files) {
            value.append(file.getAbsolutePath());
            value.append(':');
        }
        if (value.length() > 0) {
            value.setLength(value.length() - 1); // Remove trailing colon
        }
        setProperty(key, value.toString());
    }

    public Font getFont(String key) {
        String fontName = getProperty(key);
        Font font;
        if (fontName == null) {
            font = null;
        } else {
            String[] parts = fontName.split(":");
            font = new Font(parts[0], parts[1], parts[2]);
        }
        return font;
    }

    public String getString(String name) {
        return getProperty(name);
    }

    public void setString(String name, String value) {
        setProperty(name, value);
    }

    public Integer getInteger(String name) {
        String value = getProperty(name);
        Integer intValue;
        if (value == null) {
            intValue = null;
        }
        else {
            intValue = Integer.valueOf(value);
        }
        return intValue;
    }

    public void setInteger(String name, Integer value) {
        if (value != null) {
            setProperty(name, value.toString());
        }
    }

    public Boolean getBoolean(String name) {
        String value = getProperty(name);
        Boolean boolValue;
        if (value == null) {
            boolValue = null;
        }
        else {
            boolValue = Boolean.valueOf(value);
        }
        return boolValue;
    }

    public void setBoolean(String name, Boolean value) {
        if (value != null) {
            setProperty(name, value.toString());
        }
    }

    public void close(FileInputStream s, File f) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close file: " + f.getAbsolutePath() , e);
            }
        }
    }

    public void close(FileOutputStream s, File f) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close file: " + f.getAbsolutePath() , e);
            }
        }
    }
}
