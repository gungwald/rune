package com.alteredmechanism.rune;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertiesFileDataStore {


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

    protected List<File> csvToFileList(String csv) {
        List<File> files = new ArrayList<File>();
        for (String name : csv.split(":")) {
            files.add(new File(name));
        }
        return files;
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

}
