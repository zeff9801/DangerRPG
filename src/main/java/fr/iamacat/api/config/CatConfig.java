package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;
import cpw.mods.fml.common.Loader;
import java.io.File;
import java.util.List;

import java.io.*;
import java.util.*;
// TODO ADD CONFIG VERSIONING
// TODO ADD SAVE CONFIG PER KEY
// TODO SIMPLIFY CONFIG CREATION
// TODO OPTIMIZE THE CODE
// TODO ADD NON CATEGORY CONFIG SUPPORT
public class CatConfig {

    protected PropertyManager pM = new PropertyManager();
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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write("# " + category + " Configuration\n");
                writer.write("# " + new Date() + "\n");
                writer.write("# THIS CONFIG FILE IS IN ALPHA VERSION\n");
                pM.properties.forEach((key, value) -> {
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
                            String finalValue = existingValue == null || existingValue.trim().isEmpty() ? val : existingValue;
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
                pM.properties.load(fis);
            } catch (IOException e) {
                CatLogger.logger.severe("Error when loading the configuration for category " + category + ": " + e.getMessage());
            }
        }
    }
}
