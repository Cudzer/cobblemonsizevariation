package dev.cudzer.cobblemonsizevariation.config;

import com.google.gson.*;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ModConfig {
    private static final String configFileLoc = CobblemonSizeVariation.MOD_ID + "/config.json";

    public static float preventShoulderMountSize;
    public static float preventRidingMaxSize;

    public static boolean enableEssenceRecipes;

    private static Path fullPath;

    public static void init(Path platformConfigDirectory){
        fullPath = platformConfigDirectory.resolve(configFileLoc);
        final JsonObject defaultConfiguration = new JsonObject();

        addDefaultFields(defaultConfiguration);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject configuration;

        try{
            configuration = JsonParser.parseReader(new FileReader(fullPath.toString()))
                    .getAsJsonObject();
        } catch (FileNotFoundException e){
            CobblemonSizeVariation.LOGGER.warn("Could not find configuration file");
            configuration = new JsonObject();
        }

        final JsonObject finalConfiguration = configuration;

        if(defaultConfiguration.keySet().stream().anyMatch(k -> !finalConfiguration.has(k)) || finalConfiguration.keySet().stream().anyMatch(k -> !defaultConfiguration.has(k))){
            rewriteConfig(gson, defaultConfiguration, finalConfiguration);
        }

        loadConfig(finalConfiguration);
    }

    private static void addDefaultFields(JsonObject defaultConfig){
        defaultConfig.addProperty(ConfigKey.PREVENT_SHOULDER_MOUNT_SIZE, 1.5F);
        defaultConfig.addProperty(ConfigKey.PREVENT_RIDING_MAX_SIZE, 1.8F);
        defaultConfig.addProperty(ConfigKey.ENABLE_ESSENCE_RECIPES, false);
    }

    private static void rewriteConfig(Gson gson, JsonObject defaultConfig, JsonObject finalConfig){
        defaultConfig.keySet().stream()
                .filter(k -> !finalConfig.has(k))
                .forEach( k -> {
                    CobblemonSizeVariation.LOGGER.info("Adding new field '{}' to the config", k);
                    finalConfig.add(k, defaultConfig.get(k));
                });

        try{
            Files.createDirectories(Paths.get(fullPath.toString()).getParent());
            FileWriter writer = new FileWriter(fullPath.toString());
            gson.toJson(finalConfig, writer);
            writer.close();
        } catch (IOException ioException){
            CobblemonSizeVariation.LOGGER.warn("Could not create new config");
        }
    }

    private static void loadConfig(JsonObject finalConfiguration){
        preventShoulderMountSize = finalConfiguration.get(ConfigKey.PREVENT_SHOULDER_MOUNT_SIZE).getAsFloat();
        preventRidingMaxSize = finalConfiguration.get(ConfigKey.PREVENT_RIDING_MAX_SIZE).getAsFloat();
        enableEssenceRecipes = finalConfiguration.get(ConfigKey.ENABLE_ESSENCE_RECIPES).getAsBoolean();
    }
}
