package dev.cudzer.cobblemonsizevariation.fabric;

import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

public final class CobblemonSizeVariationFabric implements ModInitializer, Platform {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CobblemonSizeVariation.init(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID).resolve(MOD_ID + ".config"), true, this);
    }

    public boolean isModInstalled(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
