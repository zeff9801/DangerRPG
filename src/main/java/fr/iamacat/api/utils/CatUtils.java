package fr.iamacat.api.utils;

import fr.iamacat.api.CatLogger;
import java.io.*;
import java.util.Properties;

public class CatUtils {

    public static void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                CatLogger.logger.info("Created new directory: {}", directory.getAbsolutePath());
            } else {
                CatLogger.logger.error("Failed to create directory: {}", directory.getAbsolutePath());
            }
        }
    }

    public static void createFileIfNotExists(File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    CatLogger.logger.info("Created new file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                CatLogger.logger.error("Error creating file: {}", e.getMessage());
            }
        }
    }

    public static Properties loadProperties(File file) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            CatLogger.logger.warn("Could not read properties from file: {}", e.getMessage());
        }
        return properties;
    }

    public static void writePropertiesToFile(File file, Properties properties, String header) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(header + "\n");
            writer.write("# " + new java.util.Date() + "\n");
            properties.forEach((key, value) -> {
                try {
                    writer.write(key + "=" + value + "\n");
                } catch (IOException e) {
                    CatLogger.logger.error("Error when writing properties to file: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            CatLogger.logger.error("Error when writing properties to file: " + e.getMessage());
        }
    }

    public static void addCommentToProperties(Properties properties, String category, String comment) {
        properties.forEach((key, value) -> {
            String keyString = key.toString();
            if (keyString.startsWith(category + ".")) {
                String val = value.toString();
                properties.setProperty(keyString, val + " # " + comment);
            }
        });
    }
}
