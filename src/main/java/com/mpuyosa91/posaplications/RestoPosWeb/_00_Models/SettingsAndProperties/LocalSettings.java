package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties;

import java.io.*;
import java.util.Properties;

public class LocalSettings {

    private Properties properties;
    private String     pathname       = "local_settings.cfg";
    private File       propertiesFile = new File(pathname);

    public LocalSettings() {
        properties = new java.util.Properties();
        for (int i = 0; i < 10; i++) {
            if (loadFile()) {
                break;
            } else {
                for (int j = 0; j < 10; j++) {
                    if (createFile()) break;
                }
            }
        }
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            FileWriter fileWriter = new FileWriter(propertiesFile);
            properties.store(fileWriter, pathname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createFile() {
        try {
            properties.setProperty("host", "localhost");
            properties.setProperty("port", "8080");
            FileOutputStream fileOutputStream = new FileOutputStream(propertiesFile);
            properties.store(fileOutputStream, pathname);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean loadFile() {
        try {
            InputStream inputStream = new FileInputStream(pathname);
            properties.load(inputStream);
            return true;
        } catch (NullPointerException | IOException e) {
            return false;
        }
    }

}
