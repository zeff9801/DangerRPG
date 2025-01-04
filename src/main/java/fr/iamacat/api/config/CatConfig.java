package fr.iamacat.api.config;

import fr.iamacat.api.CatLogger;
import fr.iamacat.api.utils.CatUtils;
import cpw.mods.fml.common.Loader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
// TODO ADD SAVE CONFIG PER KEY
// TODO SIMPLIFY CONFIG CREATION
// TODO OPTIMIZE THE CODE
// TODO ADD NON CATEGORY CONFIG SUPPORT
public class CatConfig {

    protected PropertyManager pM = new PropertyManager();
    private final File configFolder;
    private final List<String> categories;
    protected static String CONFIG_VERSION = "1.0";
    private static final String VERSION_KEY = "config.version";

    public CatConfig(String folderName, List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("The list of categories must not be empty.");
        }
        this.categories = categories;
        this.configFolder = new File(new File(Loader.instance().getConfigDir(), "config"), folderName);
        CatUtils.createDirectoryIfNotExists(configFolder);
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
                writer.write("# THIS CONFIG FILE IS IN ALPHA VERSION\n");
                writer.write(VERSION_KEY + "=" + CONFIG_VERSION + "\n");
                pM.properties.forEach((key, value) -> {
                    String keyString = key.toString();
                    if (keyString.startsWith(category + ".")) {
                        String keyWithoutCategory = keyString.substring((category + ".").length());
                        String existingValue = existingProperties.getProperty(category + "." + keyWithoutCategory);
                        String val = value.toString();
                        String comment = "";
                        int commentIndex = val.indexOf(" # ");
                        if (commentIndex != -1) {
                            comment = val.substring(commentIndex + 3); // Extract the comment
                            val = val.substring(0, commentIndex); // Extract the value without comment
                        }
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
            pM.properties.putAll(existingProperties);
        }
    }
}
