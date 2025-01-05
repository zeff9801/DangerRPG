package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;
import fr.iamacat.api.utils.CatUtils;
import cpw.mods.fml.common.Loader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

// TODO ADD NON CATEGORY CONFIG SUPPORT
// TODO ADD GUI INGAME CONFIG
// TODO MADE A CRASH REPORT IF A CATEGORY IS NOT REGISTERED TO "categories" but still used
// TODO SUPPORT MAP/ANOTHER OBJECT IN loadConfig ,like in saveConfigForKey
public class CatConfig {

    protected PropertyManager pM = new PropertyManager();
    private final File configFolder;
    private final List<String> categories;
    protected static String CONFIG_VERSION = "1.0";
    private static final String VERSION_KEY = "config.version";
    protected Map<String, String> configCommentHeaders = new HashMap<>();

    public CatConfig(String folderName, List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("The list of categories must not be empty.");
        }
        this.categories = categories;
        this.configFolder = new File(new File(Loader.instance().getConfigDir(), "config"), folderName);
        CatUtils.createDirectoryIfNotExists(configFolder);
        registerProperties();
    }

    protected void setConfigCommentHeader(String category, String comment) {
        configCommentHeaders.put(category, comment);
    }

    public void loadConfig() {
        for (String category : categories) {
            File configFile = new File(configFolder, category + ".cfg");
            Properties existingProperties = CatUtils.loadProperties(configFile);
            CatUtils.createFileIfNotExists(configFile);
            String existingVersion = existingProperties.getProperty(VERSION_KEY);
            boolean versionChanged = !CONFIG_VERSION.equals(existingVersion);
            if (versionChanged) {
                existingProperties.clear();
                existingProperties.setProperty(VERSION_KEY, CONFIG_VERSION);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write("# " + category + " Configuration\n");
                writer.write("# " + new Date() + "\n");
                writer.write("\n");
                String configCommentHeader = configCommentHeaders.getOrDefault(category, "");
                if (!configCommentHeader.isEmpty()) {
                    writer.write("#########################################################################################################\n");
                    writer.write(addHashesToLines(configCommentHeader));
                    writer.write("#########################################################################################################\n");
                }
                writer.write(VERSION_KEY + "=" + CONFIG_VERSION + "\n");
                pM.registeredProperties.forEach((key, configValue) -> {
                    if (key.startsWith(category + ".")) {
                        String keyWithoutCategory = key.substring((category + ".").length());
                        String existingValue = existingProperties.getProperty(wrapKey(category + "." + keyWithoutCategory));
                        String val = pM.properties.getProperty(key).split(" # ")[0];
                        String comment = pM.properties.getProperty(key).split(" # ").length > 1 ? pM.properties.getProperty(key).split(" # ")[1] : "";
                        try {
                            writer.write("\n");
                            if (!comment.isEmpty()) {
                                writer.write(addHashesToLines(comment));
                            }
                            String finalValue;
                            if (configValue.resetValueAtGameLaunch) {
                                finalValue = serializeValue(configValue.defaultValue);
                            } else {
                                finalValue = existingValue == null || existingValue.trim().isEmpty() ? val : existingValue;
                            }
                            writer.write(wrapKey(category + "." + keyWithoutCategory) + "=" + finalValue + "\n");
                        } catch (IOException e) {
                            CatLogger.logger.error("Error when writing the configuration: {}", e.getMessage());
                        }
                    }
                });
            } catch (IOException e) {
                CatLogger.logger.error("Error when saving the configuration: {}", e.getMessage());
            }
            pM.properties.putAll(existingProperties);
            pM.loadValues();
            updateStaticFields(category);
        }
    }
    public void postLoadConfig() {
        CatLogger.logger.error("Please implement postLoadConfig method from CatConfig to use it");
    }

    private String addHashesToLines(String text) {
        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\n");

        for (String line : lines) {
            result.append("# ").append(line).append("\n");
        }

        return result.toString();
    }

    public void saveConfigForKey(String category, String key, Object value) {
        File configFile = new File(configFolder, category + ".cfg");
        CatUtils.createFileIfNotExists(configFile);
        String fullKey = category + "." + key;
        String valueToSave;
        if (value instanceof List) {
            List<?> itemList = (List<?>) value;
            StringBuilder sb = new StringBuilder();
            String indent = new String(new char[fullKey.length() + 1]).replace('\0', ' ');
            for (Object item : itemList) {
                sb.append(indent).append(item).append("\n");
            }
            valueToSave = sb.toString().trim();
        } else {
            valueToSave = serializeValue(value);
        }

        pM.properties.setProperty(fullKey, valueToSave);
        List<String> lines = new ArrayList<>();
        boolean lineFound = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(fullKey + "=")) {
                    lines.add(fullKey + "=" + valueToSave);
                    lineFound = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            CatLogger.logger.error("Error reading configuration file: {}", e.getMessage());
            return;
        }

        if (!lineFound) {
            lines.add(fullKey + "=" + valueToSave);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            CatLogger.logger.error("Error writing configuration file: {}", e.getMessage());
        }
    }

    protected void registerProperties() {
        CatLogger.logger.error("Please implement registerProperties method from CatConfig otherwise your config file will be empty");
    }

    protected void updateStaticFields(String category) {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    String propertyName = field.getName();
                    Object value = pM.getRegisteredValue(category, propertyName);
                    if (value != null) {
                        field.set(null, value);
                    }
                }
            }
        } catch (Exception e) {
            CatLogger.logger.error("Failed to update static fields: {}", e.getMessage());
        }
    }


    private String wrapKey(String key) {
        return key.contains(" ") ? "\"" + key + "\"" : key;
    }

    private String serializeValue(Object value) {
        if (value instanceof Boolean) {
            return Boolean.toString((Boolean) value);
        } else if (value instanceof Set) {
            return String.join(",", (Set<String>) value);
        } else if (value instanceof Map) {
            Map<String, String> map = (Map<String, String>) value;
            StringBuilder serializedMap = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (serializedMap.length() > 0) {
                    serializedMap.append(",");
                }
                serializedMap.append(entry.getKey()).append("=").append(entry.getValue());
            }
            return serializedMap.toString();
        } else if (value instanceof String[]) {
            return String.join(",", (String[]) value);
        } else if (value instanceof List) {
            StringBuilder sb = new StringBuilder();
            for (Object item : (List<?>) value) {
                sb.append(item).append(",");
            }
            return sb.toString().replaceAll(",$", ""); // Remove the trailing comma
        } else if (value instanceof Double) {
            return Double.toString((Double) value);
        } else if (value instanceof Float) {
            return Float.toString((Float) value);
        } else if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else if (value instanceof Long) {
            return Long.toString((Long) value);
        } else {
            return value.toString();
        }
    }



}
