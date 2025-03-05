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

public class GenIXSizer implements ISizer{

    private final SizeDefinition sizeDefinition;

    private final float minSizeModifier;
    private final float maxSizeModifier;

    public GenIXSizer(SizeDefinition definition)
    {
        this.sizeDefinition = definition;
        minSizeModifier = Float.parseFloat(this.sizeDefinition.getMinSizeModifier());
        maxSizeModifier = Float.parseFloat(this.sizeDefinition.getMaxSizeModifier());
    }

    @Override
    public float getSize() {
        //convert the integer number into a scaled float number
        int value = new Random().nextInt(0, 255);

        return minSizeModifier + ((float) value / 255) * (maxSizeModifier - minSizeModifier);
    }

    @Override
    public Size getSizeInformation(float size) {
        int value = convertSizeToInt(size);

        for(Size s : sizeDefinition.getSizes()){
            if(value >= Integer.parseInt(s.getMin()) && value <= Integer.parseInt(s.getMax())){
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

    private int convertSizeToInt(float size){
        var a = (maxSizeModifier - minSizeModifier) / 255;
        var b = size - minSizeModifier;
        var x = b / a;
        return (int)(x);
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
            CobblemonSizeVariation.LOGGER.error("File for gen9 sizer was not created.");
            throw new RuntimeException("The configured size algorithm was not loaded correctly");
        }
    }

    private static void addDefaultFields(JsonObject defaultConfig){
        defaultConfig.addProperty("name", "gen9");
        defaultConfig.addProperty("minSizeModifier", "0.2");
        defaultConfig.addProperty("maxSizeModifier", "2.0");
        defaultConfig.add("sizes", generateDefaultSizeDefinitions());
    }

    private static JsonArray generateDefaultSizeDefinitions(){
        JsonArray sizeDefinitions = new JsonArray();

        JsonObject xxxsDefinition = new JsonObject();
        xxxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XXXS");
        xxxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "0");
        xxxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "0");
        xxxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1b88cc");
        sizeDefinitions.add(xxxsDefinition);

        JsonObject xxsDefinition = new JsonObject();
        xxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XXS");
        xxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "1");
        xxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "24");
        xxsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1b88cc");
        sizeDefinitions.add(xxsDefinition);

        JsonObject xsDefinition = new JsonObject();
        xsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XS");
        xsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "25");
        xsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "59");
        xsDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1b88cc");
        sizeDefinitions.add(xsDefinition);

        JsonObject sDefinition = new JsonObject();
        sDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "S");
        sDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "60");
        sDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "99");
        sDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#1bcc9a");
        sizeDefinitions.add(sDefinition);

        JsonObject mDefinition = new JsonObject();
        mDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "M");
        mDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "100");
        mDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "155");
        mDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#ffffff");
        sizeDefinitions.add(mDefinition);

        JsonObject lDefinition = new JsonObject();
        lDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "L");
        lDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "156");
        lDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "195");
        lDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#e6ff2b");
        sizeDefinitions.add(lDefinition);

        JsonObject xlDefinition = new JsonObject();
        xlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XL");
        xlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "196");
        xlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "230");
        xlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f21800");
        sizeDefinitions.add(xlDefinition);

        JsonObject xxlDefinition = new JsonObject();
        xxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XXL");
        xxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "231");
        xxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "254");
        xxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f21800");
        sizeDefinitions.add(xxlDefinition);

        JsonObject xxxlDefinition = new JsonObject();
        xxxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_NAME, "XXXL");
        xxxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MIN, "255");
        xxxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_MAX, "255");
        xxxlDefinition.addProperty(ConfigKey.SIZE_DEFINITION_COLOR, "#f21800");
        sizeDefinitions.add(xxxlDefinition);

        return sizeDefinitions;
    }
}
