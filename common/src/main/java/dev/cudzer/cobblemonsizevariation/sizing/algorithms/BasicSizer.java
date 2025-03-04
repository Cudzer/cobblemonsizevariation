package dev.cudzer.cobblemonsizevariation.sizing.algorithms;

import com.google.gson.*;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ConfigKey;
import dev.cudzer.cobblemonsizevariation.config.Size;
import dev.cudzer.cobblemonsizevariation.sizing.SizeDefinition;
import dev.cudzer.cobblemonsizevariation.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Random;

public class BasicSizer implements ISizer{

    private final SizeDefinition sizeDefinition;

    private final float minSizeModifier;
    private final float maxSizeModifier;

    public BasicSizer(SizeDefinition sizeDefinition){
        this.sizeDefinition = sizeDefinition;
        minSizeModifier = Float.parseFloat(this.sizeDefinition.getMinSizeModifier());
        maxSizeModifier = Float.parseFloat(this.sizeDefinition.getMaxSizeModifier());
    }

    @Override
    public float getSize() {
        return new Random().nextFloat() * (
                maxSizeModifier - minSizeModifier)
                + minSizeModifier;
    }

    @Override
    public Size getSizeInformation(float size) {
        for(Size s : sizeDefinition.getSizes()){
            if(size >= Float.parseFloat(s.getMin()) && size <= Float.parseFloat(s.getMax())){
                return s;
            }
        }
        return null;
    }

    public float getMinSizeModifier(){
        return this.minSizeModifier;
    }

    public float getMaxSizeModifier(){
        return this.maxSizeModifier;
    }

    public static JsonElement createConfig(Path sizeFile){
        final JsonObject defaultConfiguration = new JsonObject();

        addDefaultFields(defaultConfiguration);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject configuration;
        try{
            configuration = JsonParser.parseReader(new FileReader(sizeFile.toString())).getAsJsonObject();
        }
        catch (FileNotFoundException e){
            configuration = FileUtils.createFile(gson, defaultConfiguration, sizeFile);
        }

        if(configuration != null){
            return configuration;
        }
        else{
            CobblemonSizeVariation.LOGGER.error("File for basic sizer was not created.");
            throw new RuntimeException("The configured size algorithm was not loaded correctly");
        }
    }

    private static void addDefaultFields(JsonObject defaultConfig){
        defaultConfig.addProperty("name", "basic");
        defaultConfig.addProperty("minSizeModifier", "0.2");
        defaultConfig.addProperty("maxSizeModifier", "2.0");
        defaultConfig.add("sizes", generateDefaultSizeDefinitions());
    }

    private static JsonArray generateDefaultSizeDefinitions(){
        JsonArray sizeDefinitions = new JsonArray();

        JsonObject tinyDefinition = new JsonObject();
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Tiny");
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "0.2");
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "0.5");
        tinyDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1b88cc");
        sizeDefinitions.add(tinyDefinition);

        JsonObject smallDefinition = new JsonObject();
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Small");
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "0.51");
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "0.9");
        smallDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1bcc9a");
        sizeDefinitions.add(smallDefinition);

        JsonObject averageDefinition = new JsonObject();
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Average");
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "0.91");
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "1.2");
        averageDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#ffffff");
        sizeDefinitions.add(averageDefinition);

        JsonObject bigDefinition = new JsonObject();
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Big");
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "1.21");
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "1.6");
        bigDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#e6ff2b");
        sizeDefinitions.add(bigDefinition);

        JsonObject largeDefinition = new JsonObject();
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Large");
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "1.61");
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "1.9");
        largeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f07426");
        sizeDefinitions.add(largeDefinition);

        JsonObject hugeDefinition = new JsonObject();
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "Huge");
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "1.91");
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "2.0");
        hugeDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f21800");
        sizeDefinitions.add(hugeDefinition);

        return sizeDefinitions;
    }
}
