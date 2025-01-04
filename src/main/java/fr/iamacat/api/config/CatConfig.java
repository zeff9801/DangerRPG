package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.*;
// TODO FIX CONFIG ARE LOADED AFTER MIXINS GET INJECTED
public class CatConfig {

    private final Properties properties = new Properties();
    private final File configFolder;
    private final List<String> categories;

    public CatConfig(String folderName, List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("The list of categories must not be empty.");
        }
        this.categories = categories;
        this.configFolder = new File(Minecraft.getMinecraft().mcDataDir, "config/" + folderName);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }

    public void loadConfig() {
        for (String category : categories) {
            File configFile = new File(configFolder, category + ".cfg");
            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    CatLogger.logger.severe("Error creating the configuration file for category " + category + ": " + e.getMessage());
                    continue;
                }
            }
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                CatLogger.logger.severe("Error when loading the configuration for category " + category + ": " + e.getMessage());
            }
        }
    }

    public void saveConfig() {
        for (String category : categories) {
            File configFile = new File(configFolder, category + ".cfg");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write("# Configuration " + categories + "\n");
                writer.write("# " + new Date() + "\n");
                writer.write("# THIS CONFIG FILE IS IN ALPHA VERSION , SO MANY BUGS AND NOT FINISHED");
                writer.write("\n");
                properties.forEach((key, value) -> {
                    if (key.toString().startsWith(category + ".")) {
                        try {
                            String comment = "";
                            String val = value.toString();
                            int commentIndex = val.indexOf(" # ");
                            if (commentIndex != -1) {
                                comment = val.substring(commentIndex + 3);
                                val = val.substring(0, commentIndex);
                            }
                            writer.write("\n");
                            if (!comment.isEmpty()) {
                                writer.write("# " + comment + "\n");
                            }
                            writer.write(key.toString().substring((category + ".").length()) + "=" + val + "\n");
                        } catch (IOException e) {
                            CatLogger.logger.severe("Error when writing the configuration: " + e.getMessage());
                        }
                    }
                });
            } catch (IOException e) {
                CatLogger.logger.severe("Error when saving the configuration: " + e.getMessage());
            }
        }
    }

    // Méthodes génériques pour récupérer les propriétés avec catégorie et commentaire
    public String getProperty(String category, String key, String defaultValue, String comment) {
        // Ajouter le commentaire si la clé n'existe pas encore
        if (!properties.containsKey(category + "." + key)) {
            properties.setProperty(category + "." + key, defaultValue + " # " + comment);
        }
        return properties.getProperty(category + "." + key, defaultValue).split(" # ")[0];
    }

    public boolean getProperty(String category, String key, boolean defaultValue, String comment) {
        // Ajouter le commentaire si la clé n'existe pas encore
        if (!properties.containsKey(category + "." + key)) {
            properties.setProperty(category + "." + key, Boolean.toString(defaultValue) + " # " + comment);
        }
        String value = properties.getProperty(category + "." + key, Boolean.toString(defaultValue)).split(" # ")[0];
        return Boolean.parseBoolean(value);
    }

    public int getProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
        // Ajouter le commentaire si la clé n'existe pas encore
        if (!properties.containsKey(category + "." + key)) {
            properties.setProperty(category + "." + key, defaultValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
        }
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public float getProperty(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
        // Ajouter le commentaire si la clé n'existe pas encore
        if (!properties.containsKey(category + "." + key)) {
            properties.setProperty(category + "." + key, defaultValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
        }
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getProperty(String category, String key, double defaultValue, double minValue, double maxValue, String comment) {
        // Ajouter le commentaire si la clé n'existe pas encore
        if (!properties.containsKey(category + "." + key)) {
            properties.setProperty(category + "." + key, defaultValue + " # " + comment + " (min: " + minValue + ", max: " + maxValue + ")");
        }
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
        properties.setProperty(category + "." + key, Boolean.toString(value) + " # " + comment);
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
