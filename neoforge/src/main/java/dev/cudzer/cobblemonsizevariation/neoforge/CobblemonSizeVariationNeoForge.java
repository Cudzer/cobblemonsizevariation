package dev.cudzer.cobblemonsizevariation.neoforge;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

@Mod(MOD_ID)
public final class CobblemonSizeVariationNeoForge implements Platform {
    public CobblemonSizeVariationNeoForge() {
        // Run our common setup.
        CobblemonSizeVariation.init(this);
    }

    public boolean isModInstalled(String modId){
        return ModList.get().isLoaded(modId);
    }

    public Path getConfigDirectory(){
        return FMLPaths.CONFIGDIR.get();
    }
}
