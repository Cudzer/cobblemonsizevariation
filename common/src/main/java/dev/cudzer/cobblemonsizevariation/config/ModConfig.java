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
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    private static final String configFileLoc = CobblemonSizeVariation.MOD_ID + "/config.json";

    public static float minSizeMultiplier;
    public static float maxSizeMultiplier;

    public static float preventShoulderMountSize;
    public static float sizeModificationChance;

    private static JsonArray sizeDefinitionConfig;
    public static List<SizeDefinition> sizeDefinitions = new ArrayList<>();

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

        if(defaultConfiguration.keySet().stream().anyMatch(k -> !finalConfiguration.has(k))){
            rewriteConfig(gson, defaultConfiguration, finalConfiguration);
        }

        loadConfig(finalConfiguration);
    }

    private static void addDefaultFields(JsonObject defaultConfig){
        defaultConfig.addProperty(ConfigKey.SIZE_MODIFICATION_CHANCE, 0.5F);
        defaultConfig.addProperty(ConfigKey.PREVENT_SHOULDER_MOUNT_SIZE, 1.5F);
        defaultConfig.addProperty(ConfigKey.MINIMUM_SIZE_MULTIPLIER, 0.2F);
        defaultConfig.addProperty(ConfigKey.MAXIMUM_SIZE_MULTIPLIER, 2.0F);

        defaultConfig.add(ConfigKey.SIZE_DEFINITIONS, generateDefaultSizeDefinitions());
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
        sizeModificationChance = finalConfiguration.get(ConfigKey.SIZE_MODIFICATION_CHANCE).getAsFloat();
        preventShoulderMountSize = finalConfiguration.get(ConfigKey.PREVENT_SHOULDER_MOUNT_SIZE).getAsFloat();
        minSizeMultiplier = finalConfiguration.get(ConfigKey.MINIMUM_SIZE_MULTIPLIER).getAsFloat();
        maxSizeMultiplier = finalConfiguration.get(ConfigKey.MAXIMUM_SIZE_MULTIPLIER).getAsFloat();
        sizeDefinitionConfig = finalConfiguration.get(ConfigKey.SIZE_DEFINITIONS).getAsJsonArray();

        sizeDefinitions.clear();
        sizeDefinitionConfig.iterator().forEachRemaining(
                (element) -> sizeDefinitions.add(parseSizeDefinitionElement(element.getAsJsonObject()))
        );
    }

    public static SizeDefinition getSizeDefinition(float size){
        return sizeDefinitions.stream().filter(s -> s.isInRange(size)).findFirst().orElse(null);
    }

    private static SizeDefinition parseSizeDefinitionElement(JsonObject sizeDefinitionElement){
        String name = sizeDefinitionElement.get(ConfigKey.SIZE_DEFINITION_NAME).getAsString();
        float min = sizeDefinitionElement.get(ConfigKey.SIZE_DEFINITION_MIN).getAsFloat();
        float max = sizeDefinitionElement.get(ConfigKey.SIZE_DEFINITION_MAX).getAsFloat();
        String color = sizeDefinitionElement.get(ConfigKey.SIZE_DEFINITION_COLOR).getAsString();

        return new SizeDefinition(name, min, max, color);
    }

    private static JsonArray generateDefaultSizeDefinitions(){
        JsonArray sizeDefinitions = new JsonArray();

        JsonObject tinyDefinition = new JsonObject();
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Tiny");
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 0.2F);
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 0.5F);
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1b88cc");
        sizeDefinitions.add(tinyDefinition);

        JsonObject smallDefinition = new JsonObject();
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Small");
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 0.51F);
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 0.9F);
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1bcc9a");
        sizeDefinitions.add(smallDefinition);

        JsonObject averageDefinition = new JsonObject();
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Average");
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 0.91F);
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 1.2F);
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#ffffff");
        sizeDefinitions.add(averageDefinition);

        JsonObject bigDefinition = new JsonObject();
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Big");
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 1.21F);
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 1.6F);
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#e6ff2b");
        sizeDefinitions.add(bigDefinition);

        JsonObject largeDefinition = new JsonObject();
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Large");
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 1.61F);
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 1.9F);
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#fa892d");
        sizeDefinitions.add(largeDefinition);

        JsonObject hugeDefinition = new JsonObject();
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Huge");
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, 1.91F);
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, 2.0F);
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f21800");
        sizeDefinitions.add(hugeDefinition);


        return sizeDefinitions;
    }
}
