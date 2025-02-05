package dev.cudzer.cobblemonsizevariation.fabric;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class CobblemonSizeVariationFabric implements ModInitializer, Platform {
    @Override
    public void onInitialize() {
        // Run our common setup.
        CobblemonSizeVariation.init(this);

        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandBuildContext, commandSelection) -> {
            CobblemonSizeVariation.registerCommands(commandDispatcher);
        }));
    }

    public boolean isModInstalled(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public Path getConfigDirectory(){
        return FabricLoader.getInstance().getConfigDir();
    }
}
