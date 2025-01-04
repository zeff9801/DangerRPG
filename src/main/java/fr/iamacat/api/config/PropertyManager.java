package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;

import java.util.Properties;

public class PropertyManager {
    public final Properties properties = new Properties();

    public String getProperty(String category, String key, String defaultValue) {
        return properties.getProperty(category + "." + key, defaultValue).split(" # ")[0];
    }

    public boolean getProperty(String category, String key, boolean defaultValue) {
        String value = properties.getProperty(category + "." + key, Boolean.toString(defaultValue)).split(" # ")[0];
        return Boolean.parseBoolean(value);
    }

    public int getProperty(String category, String key, int defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        return Integer.parseInt(value);
    }

    public float getProperty(String category, String key, float defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        return Float.parseFloat(value);
    }

    public double getProperty(String category, String key, double defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue + "").split(" # ")[0];
        return Double.parseDouble(value);
    }

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
