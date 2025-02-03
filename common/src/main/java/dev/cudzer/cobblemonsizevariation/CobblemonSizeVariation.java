package dev.cudzer.cobblemonsizevariation;

import dev.cudzer.cobblemonsizevariation.config.CobblemonSizeVariationConfig;
import dev.cudzer.cobblemonsizevariation.event.ModEvents;

import java.nio.file.Path;
import java.util.logging.Logger;

public final class CobblemonSizeVariation {
    public static final String MOD_ID = "cobblemonsizevariation";

    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    public static Path configPath;
    public static Platform platform;
    public static ModDependencyChecker dependencyChecker;

    public static void init(Path configPath, boolean useConfig, Platform modPlatform) {
        platform = modPlatform;
        dependencyChecker = new ModDependencyChecker(platform);
        dependencyChecker.checkDependencies();
        if(useConfig){
            CobblemonSizeVariation.configPath = configPath;
            CobblemonSizeVariationConfig.init();
        }
        ModEvents.registerEvents();
    }
}
