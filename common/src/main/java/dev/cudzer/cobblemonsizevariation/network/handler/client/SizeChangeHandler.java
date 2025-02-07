package dev.cudzer.cobblemonsizevariation.network.handler.client;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class SizeChangeHandler implements ClientNetworkPacketHandler<SizeChangedPacket> {
    @Override
    public void handle(@NotNull SizeChangedPacket sizeChangedPacket, @NotNull Minecraft minecraft) {
        sizeChangedPacket.applyToPokemon();
    }
}
