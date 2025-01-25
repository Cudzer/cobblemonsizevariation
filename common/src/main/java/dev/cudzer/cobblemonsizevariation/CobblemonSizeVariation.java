package dev.cudzer.cobblemonsizevariation;

import dev.cudzer.cobblemonsizevariation.config.CobblemonSizeVariationConfig;
import dev.cudzer.cobblemonsizevariation.event.ModEvents;

import java.nio.file.Path;
import java.util.logging.Logger;

public final class CobblemonSizeVariation {
    public static final String MOD_ID = "cobblemonsizevariation";

    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    public static Path configPath;

    public static void init(Path configPath, boolean useConfig) {
        if(useConfig){
            CobblemonSizeVariation.configPath = configPath;
            CobblemonSizeVariationConfig.init();
        }
        ModEvents.registerEvents();
    }
}
