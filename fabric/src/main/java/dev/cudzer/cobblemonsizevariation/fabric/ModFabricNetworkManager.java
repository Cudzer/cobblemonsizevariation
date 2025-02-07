package dev.cudzer.cobblemonsizevariation.fabric;

import com.cobblemon.mod.common.NetworkManager;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.fabric.net.FabricPacketInfo;
import dev.cudzer.cobblemonsizevariation.network.ModNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ModFabricNetworkManager implements NetworkManager {

    public void registerMessages(){
        ModNetwork.s2cPayloads.stream().map(FabricPacketInfo::new).forEach(fp -> fp.registerPacket(true));
    }

    public static void registerClientHandlers() {
        ModNetwork.s2cPayloads.stream().map(FabricPacketInfo::new).forEach(FabricPacketInfo::registerClientHandler);
    }

    @Override
    public void sendPacketToPlayer(@NotNull ServerPlayer serverPlayer, @NotNull NetworkPacket<?> networkPacket) {
        ServerPlayNetworking
                .send(serverPlayer, networkPacket);
    }

    @Override
    public void sendToServer(@NotNull NetworkPacket<?> networkPacket) {
        ClientPlayNetworking.send(networkPacket);
    }
}
