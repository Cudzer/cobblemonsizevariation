package dev.cudzer.cobblemonsizevariation.neoforge;

import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

@Mod(MOD_ID)
public final class CobblemonSizeVariationNeoForge {
    public CobblemonSizeVariationNeoForge() {
        // Run our common setup.
        CobblemonSizeVariation.init(FMLPaths.CONFIGDIR.get().resolve(MOD_ID).resolve(MOD_ID + ".config"), true);
    }
}
