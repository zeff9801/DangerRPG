package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;
import cpw.mods.fml.common.Loader;
import java.io.File;
import java.util.List;

import java.io.*;
import java.util.*;
// TODO ADD CONFIG VERSIONNING
// TODO ADD SAVE CONFIG PER KEY
// TODO SIMPLIFY CONFIG CREATION
// TODO OPTIMIZE THE CODE
// TODO ADD NON CATEGORY CONFIG SUPPORT
public class CatConfig {

    private final Properties properties = new Properties();
    private final File configFolder;
    private final List<String> categories;

    public CatConfig(String folderName, List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("The list of categories must not be empty.");
        }
        this.categories = categories;
        this.configFolder = new File(new File(Loader.instance().getConfigDir(), "config"), folderName);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }

    public void loadConfig() {
        initConfig();
    }
    private void initConfig() {
        for (String category : categories) {
            File configFile = new File(configFolder, category + ".cfg");
            Properties existingProperties = new Properties();

            // Check if the config file exists, create it if necessary
            if (!configFile.exists()) {
                try {
                    if (configFile.createNewFile()) {
                        CatLogger.logger.info("Created new configuration file: " + configFile.getAbsolutePath());
                    }
                } catch (IOException e) {
                    CatLogger.logger.severe("Error creating the configuration file for category " + category + ": " + e.getMessage());
                    continue;
                }
            }

            // Load existing properties only once
            try (FileInputStream fis = new FileInputStream(configFile)) {
                existingProperties.load(fis);
            } catch (IOException e) {
                CatLogger.logger.warning("Could not read existing configuration: " + e.getMessage());
            }

            // Write the properties (comment header and values) in one go
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                // Write the header once at the beginning
                writer.write("# Configuration " + category + "\n");
                writer.write("# " + new Date() + "\n");
                writer.write("# THIS CONFIG FILE IS IN ALPHA VERSION, SO MANY BUGS AND NOT FINISHED");
                writer.write("\n");

                // Write properties for the category
                properties.forEach((key, value) -> {
                    String keyString = key.toString();
                    if (keyString.startsWith(category + ".")) {
                        String keyWithoutCategory = keyString.substring((category + ".").length());
                        String existingValue = existingProperties.getProperty(category + "." + keyWithoutCategory);

                        // Check if there's a comment in the value (after #)
                        String val = value.toString();
                        String comment = "";
                        int commentIndex = val.indexOf(" # ");
                        if (commentIndex != -1) {
                            comment = val.substring(commentIndex + 3); // Extract the comment
                            val = val.substring(0, commentIndex); // Extract the value without comment
                        }
                        // Write the comment (if any) and the value (either existing or new)
                        try {
                            writer.write("\n");
                            if (!comment.isEmpty()) {
                                writer.write("# " + comment + "\n");
                            }

                            // Check if the existing value is empty or null
                            String finalValue = existingValue == null || existingValue.trim().isEmpty() ? val : existingValue;

                            // Write the property with its category and the correct value (either default or existing)
                            writer.write(category + "." + keyWithoutCategory + "=" + finalValue + "\n");
                        } catch (IOException e) {
                            CatLogger.logger.severe("Error when writing the configuration: " + e.getMessage());
                        }

                    }
                });
            } catch (IOException e) {
                CatLogger.logger.severe("Error when saving the configuration: " + e.getMessage());
            }

            // Load properties into memory (this was happening twice before, now it's done after writing)
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                CatLogger.logger.severe("Error when loading the configuration for category " + category + ": " + e.getMessage());
            }
        }
    }


    // Méthodes génériques pour récupérer les propriétés avec catégorie et commentaire
    public String getProperty(String category, String key, String defaultValue) {
        return properties.getProperty(category + "." + key, defaultValue).split(" # ")[0];
    }

    public boolean getProperty(String category, String key, boolean defaultValue) {
        String value = properties.getProperty(category + "." + key, Boolean.toString(defaultValue)).split(" # ")[0];
        return Boolean.parseBoolean(value);
    }

    public int getProperty(String category, String key, int defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public float getProperty(String category, String key, float defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getProperty(String category, String key, double defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Méthodes génériques pour définir les propriétés avec catégorie et commentaire
    public void setProperty(String category, String key, String value, String comment) {
        properties.setProperty(category + "." + key, value + " # " + comment);
    }

    public void setProperty(String category, String key, boolean value, String comment) {
        properties.setProperty(category + "." + key, value + " # " + comment);
    }

    public void setProperty(String category, String key, int value, int minValue, int maxValue, String comment) {
        int clampedValue = Math.min(Math.max(value, minValue), maxValue);
        if (clampedValue != value) {
            CatLogger.logger.warning("The value of " + key + " in category " + category + " was modified from " + value + " to " + clampedValue + " because it was outside the range [" + minValue + ", " + maxValue + "].");
        }
        properties.setProperty(category + "." + key, clampedValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
    }

    public void setProperty(String category, String key, float value, float minValue, float maxValue, String comment) {
        float clampedValue = Math.min(Math.max(value, minValue), maxValue);
        if (clampedValue != value) {
            CatLogger.logger.warning("The value of " + key + " in category " + category + " was modified from " + value + " to " + clampedValue + " because it was outside the range [" + minValue + ", " + maxValue + "].");
        }
        properties.setProperty(category + "." + key, clampedValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
    }

    public void setProperty(String category, String key, double value, double minValue, double maxValue, String comment) {
        double clampedValue = Math.min(Math.max(value, minValue), maxValue);
        if (clampedValue != value) {
            CatLogger.logger.warning("The value of " + key + " in category " + category + " was modified from " + value + " to " + clampedValue + " because it was outside the range [" + minValue + ", " + maxValue + "].");
        }
        properties.setProperty(category + "." + key, clampedValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
    }
}
