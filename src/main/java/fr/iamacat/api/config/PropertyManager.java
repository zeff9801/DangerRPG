package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//TODO DEDUPPLICATE CODE
public class PropertyManager {
    public final Properties properties = new Properties();
    private final Map<String, ConfigValue<?>> registeredProperties = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String category, String key, T defaultValue) {
        String value = properties.getProperty(category + "." + key, defaultValue.toString()).split(" # ")[0];

        if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(value);
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(value);
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(value);
        } else if (defaultValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else {
            return (T) value;
        }
    }

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

    public void registerProperty(String category, String key, Object defaultValue, String comment) {
        String fullKey = category + "." + key;
        if (defaultValue instanceof Boolean) {
            ConfigValue<Boolean> configValue = new ConfigValue<>((Boolean) defaultValue, comment);
            registeredProperties.put(fullKey, configValue);
        } else if (defaultValue instanceof Integer) {
            ConfigValue<Integer> configValue = new ConfigValue<>((Integer) defaultValue, comment);
            registeredProperties.put(fullKey, configValue);
        } else if (defaultValue instanceof Float) {
            ConfigValue<Float> configValue = new ConfigValue<>((Float) defaultValue, comment);
            registeredProperties.put(fullKey, configValue);
        } else if (defaultValue instanceof Double) {
            ConfigValue<Double> configValue = new ConfigValue<>((Double) defaultValue, comment);
            registeredProperties.put(fullKey, configValue);
        } else if (defaultValue instanceof String) {
            ConfigValue<String> configValue = new ConfigValue<>((String) defaultValue, comment);
            registeredProperties.put(fullKey, configValue);
        }
        properties.setProperty(fullKey, defaultValue + " # " + comment);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRegisteredValue(String category, String key) {
        String fullKey = category + "." + key;
        ConfigValue<?> configValue = registeredProperties.get(fullKey);
        if (configValue != null) {
            return (T) configValue.currentValue;
        }
        return null;
    }

    public void loadValues() {
        registeredProperties.forEach((fullKey, configValue) -> {
            String[] parts = fullKey.split("\\.", 2);
            String category = parts[0];
            String key = parts[1];

            Object defaultValue = configValue.defaultValue;
            if (defaultValue instanceof Boolean) {
                ((ConfigValue<Boolean>) configValue).currentValue =
                    getProperty(category, key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Integer) {
                ((ConfigValue<Integer>) configValue).currentValue =
                    getProperty(category, key, (Integer) defaultValue);
            } else if (defaultValue instanceof Float) {
                ((ConfigValue<Float>) configValue).currentValue =
                    getProperty(category, key, (Float) defaultValue);
            } else if (defaultValue instanceof Double) {
                ((ConfigValue<Double>) configValue).currentValue =
                    getProperty(category, key, (Double) defaultValue);
            } else if (defaultValue instanceof String) {
                ((ConfigValue<String>) configValue).currentValue =
                    getProperty(category, key, (String) defaultValue);
            }
        });
    }

    private static class ConfigValue<T> {
        final T defaultValue;
        final String comment;
        T currentValue;

        ConfigValue(T defaultValue, String comment) {
            this.defaultValue = defaultValue;
            this.comment = comment;
            this.currentValue = defaultValue;
        }
    }
}
