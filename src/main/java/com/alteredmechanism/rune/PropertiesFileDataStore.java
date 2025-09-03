package com.alteredmechanism.rune;

import com.alteredmechanism.rune.actions.SaveAction;

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

    public PropertiesFileDataStore(String fileName) throws IOException, FileCreationException {
        file = new File(fileName);
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
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
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

    public List<File> getFileList(String key) {
        List<File> files = new ArrayList<File>();
        for (String name : getProperty(key).split(":")) {
            files.add(new File(name));
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

    public boolean getBoolean(String name) {
        String value = getProperty(name);
        if (value == null) {
            System.err.println("Missing property: " + name);
        }
        else {
            System.out.println("Loaded property: " + name + "=" + value);
        }
        return Boolean.parseBoolean(value);
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
