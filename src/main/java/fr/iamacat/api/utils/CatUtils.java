package fr.iamacat.api.utils;

import fr.iamacat.api.CatLogger;
import java.io.*;

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
}
