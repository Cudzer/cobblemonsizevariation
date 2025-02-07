package dev.cudzer.cobblemonsizevariation.fabric.client;

import dev.cudzer.cobblemonsizevariation.fabric.ModFabricNetworkManager;
import net.fabricmc.api.ClientModInitializer;

public final class CobblemonSizeVariationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModFabricNetworkManager.registerClientHandlers();
    }
}
