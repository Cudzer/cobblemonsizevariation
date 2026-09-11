package dev.cudzer.cobblemonsizevariation.fabric;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import dev.cudzer.cobblemonsizevariation.fabric.events.EntityInteractEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class CobblemonSizeVariationFabric implements ModInitializer, Platform {

    @Override
    public void onInitialize() {
        // Run our common setup.
        CobblemonSizeVariation.init(this);
        EntityInteractEvents.register();
    }

    public boolean isModInstalled(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public Path getConfigDirectory(){
        return FabricLoader.getInstance().getConfigDir();
    }
}
