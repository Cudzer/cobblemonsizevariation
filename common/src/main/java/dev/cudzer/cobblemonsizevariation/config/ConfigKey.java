package dev.cudzer.cobblemonsizevariation.config;

public class ConfigKey {
    // Cobblemon owns the rolling, the range and the brackets, so the options that
    // used to describe them are gone: sizeModificationChance, sizingAlgorithm,
    // biasSizeTowardAverage and the size definition keys. What is left are the
    // two limits Cobblemon has no equivalent for, plus the recipe switch.
    //
    // preventRidingMinSize is gone too, on purpose: Cobblemon enforces a minimum
    // of its own through minimumRidingScale, and two guards on the same rule only
    // make it harder to work out which one refused.
    public static final String PREVENT_SHOULDER_MOUNT_SIZE = "preventShoulderMountSize";
    public static final String PREVENT_RIDING_MAX_SIZE = "preventRidingMaxSize";
    //ITEM RECIPES
    public static final String ENABLE_ESSENCE_RECIPES = "enableEssenceRecipes";
}
