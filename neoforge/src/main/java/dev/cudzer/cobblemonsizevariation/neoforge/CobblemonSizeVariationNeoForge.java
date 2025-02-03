package dev.cudzer.cobblemonsizevariation.neoforge;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

@Mod(MOD_ID)
public final class CobblemonSizeVariationNeoForge implements Platform {
    public CobblemonSizeVariationNeoForge() {
        // Run our common setup.
        CobblemonSizeVariation.init(FMLPaths.CONFIGDIR.get().resolve(MOD_ID).resolve(MOD_ID + ".config"), true, this);
    }

    public boolean isModInstalled(String modId){
        return ModList.get().isLoaded(modId);
    }
}
