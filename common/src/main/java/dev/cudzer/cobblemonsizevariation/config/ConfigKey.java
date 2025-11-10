package dev.cudzer.cobblemonsizevariation.config;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;

public class ConfigKey {

    public static final String sizerDirectoryLoc = CobblemonSizeVariation.MOD_ID + "/sizes";

    public static final String SIZE_MODIFICATION_CHANCE = "sizeModificationChance";
    public static final String PREVENT_SHOULDER_MOUNT_SIZE = "preventShoulderMountSize";
    public static final String MINIMUM_SIZE_MULTIPLIER = "minSizeMultiplier";
    public static final String MAXIMUM_SIZE_MULTIPLIER = "maxSizeMultiplier";
    //public static final String SIZE_DEFINITIONS = "sizeDefinitions";
    public static final String SIZING_ALGORITHM = "sizingAlgorithm";
    public static final String BIAS_SIZE_TOWARD_AVERAGE = "biasSizeTowardAverage";

    //SIZE DEFINITION KEYS
    public static final String SIZE_DEFINITION_NAME = "name";
    public static final String SIZE_DEFINITION_MIN = "min";
    public static final String SIZE_DEFINITION_MAX = "max";
    public static final String SIZE_DEFINITION_COLOR = "color";

    //PERMISSION KEYS
    public static final String PERMISSIONS = "permissions";
    public static final String POKESIZER_PERM_NAME = "cobblemonsizevariation.command.pokesizer";
    public static final String POKESIZER_SELF_PERM_NAME = "cobblemonsizevariation.command.pokesizer.self";
}
