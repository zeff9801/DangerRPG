package fr.iamacat.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties = new Properties();
    private static File configFile;

    public static void loadConfig(String customPath) {
        configFile = new File(customPath != null ? customPath : "config/DangerRPG.properties");
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la configuration : " + e.getMessage());
        }

        // Charger les valeurs de configuration
        loadSettings();

        // Sauvegarder les modifications, le cas échéant
        saveConfig();
    }

    private static void loadSettings() {
        // Exemple de configuration booléenne
        boolean enableFeature = Boolean.parseBoolean(properties.getProperty("enableFeature", "true"));

        // Exemple de configuration entière
        int featureValue = Integer.parseInt(properties.getProperty("featureValue", "10"));

        // Ajouter vos configurations ici
    }

    public static void saveConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            properties.store(fos, "DangerRPG Configuration");
            fos.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde de la configuration : " + e.getMessage());
        }
    }
}
