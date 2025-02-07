package dev.cudzer.cobblemonsizevariation.neoforge;

import com.cobblemon.mod.common.NetworkManager;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.Platform;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.nio.file.Path;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

@Mod(MOD_ID)
public final class CobblemonSizeVariationNeoForge implements Platform {

    ModNeoForgeNetworkManager networkManager = new ModNeoForgeNetworkManager();

    public CobblemonSizeVariationNeoForge() {
        // Run our common setup.
        CobblemonSizeVariation.init(this);
        NeoForge.EVENT_BUS.register(this);
    }

    public boolean isModInstalled(String modId){
        return ModList.get().isLoaded(modId);
    }

    public Path getConfigDirectory(){
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @SubscribeEvent
    public void onCommandRegistration(final RegisterCommandsEvent event){
        CobblemonSizeVariation.registerCommands(event.getDispatcher());
    }
}
