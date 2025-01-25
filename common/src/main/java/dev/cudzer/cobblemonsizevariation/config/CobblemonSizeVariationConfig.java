package dev.cudzer.cobblemonsizevariation.config;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.LOGGER;

public class CobblemonSizeVariationConfig {
    public static Path configPath = CobblemonSizeVariation.configPath;
    public static double configVersion = 1.0;

    public static float minSizeMultiplier = 0.2F;
    public static float maxSizeMultiplier = 2.0F;

    public static float preventShoulderMountSize = 1.5F;
    public static float sizeModificationChance = 0.5F;

    public static void init(){
        File configFile = configPath.toFile();
        load(configFile);
    }

    private static void load(File configFile){
        if(!configFile.exists()){
            createDefaultConfig(configFile);
            return;
        }
        try{
            for (String line : Files.readAllLines(configPath)){
                if(line.isEmpty() || line.startsWith("#")){
                    continue;
                }
                String[] split = line.split(":");
                if(split.length != 2){
                    LOGGER.warning("Failed to parse the config line: " + line);
                }

                String key = split[0].trim();
                String value = split[1].trim();

                switch (key){
                    case "configVersion":
                        if(Double.parseDouble(value) < configVersion){
                            LOGGER.info("The config for CobblemonSizeVariations is outdated, updating it...");
                            createDefaultConfig(configFile);
                        }
                        break;
                    case "sizeModificationChance":
                        sizeModificationChance = Float.parseFloat(value);
                        break;
                    case "preventShoulderMountSize":
                        preventShoulderMountSize = Float.parseFloat(value);
                        break;
                    case "minSizeMultiplier":
                        minSizeMultiplier = Float.parseFloat(value);
                        break;
                    case "maxSizeMultiplier":
                        maxSizeMultiplier = Float.parseFloat(value);
                        break;
                    default:
                        LOGGER.warning("Unknown config key: " + key);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load config file for Cobblemon Size Variations. This is a critical error and will cause crashes. " + e);
        }
    }

    public static void save(){
        File configFile = configPath.toFile();
        createDefaultConfig(configFile);
    }

    private static void createDefaultConfig(File configFile){
        if(!configFile.exists()){
            try{
                Files.createDirectories(configPath.getParent());
                if(!Files.exists(configPath)){
                    Files.createFile(configPath);
                }
            } catch (IOException e){
                LOGGER.warning("Failed to create config directory or file. Please report this to Cudzer " + e);
            }
        }
        try{
            List<String> defaultConfig = new ArrayList<>();
            defaultConfig.add("# The config version for the mod. DO NOT change this");
            defaultConfig.add("configVersion: " + configVersion);
            defaultConfig.add("");
            defaultConfig.add("# The chance that a cobblemons size will be changed. Values: 0.0 - 1.0");
            defaultConfig.add("sizeModificationChance: " + sizeModificationChance);
            defaultConfig.add("# The max size modification of a cobblemon that makes it too big to sit on a shoulder. Default: 1.5x modifier");
            defaultConfig.add("preventShoulderMountSize: " + preventShoulderMountSize);
            defaultConfig.add("");
            defaultConfig.add("# The minimum size scale a cobblemons size can changed to. Default: 0.2");
            defaultConfig.add("minSizeMultiplier: " + minSizeMultiplier);
            defaultConfig.add("# The maximum size scale a cobblemons size can changed to. Default: 2.0");
            defaultConfig.add("maxSizeMultiplier: " + maxSizeMultiplier);

            Files.write(configPath, defaultConfig);
        }catch (Exception e){
            LOGGER.warning("Failed to populate default config values for CobblemonSizeVariation. " + e);
        }
    }
}
