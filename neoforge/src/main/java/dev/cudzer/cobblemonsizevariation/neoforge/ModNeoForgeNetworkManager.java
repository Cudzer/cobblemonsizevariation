package dev.cudzer.cobblemonsizevariation.neoforge;

import com.cobblemon.mod.common.NetworkManager;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.neoforge.net.NeoForgePacketInfo;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

@EventBusSubscriber(modid = CobblemonSizeVariation.MOD_ID)
public class ModNeoForgeNetworkManager implements NetworkManager {
    private static final String PROTOCOL_VERSION = "1.0";

    @SubscribeEvent
    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        CobblemonSizeVariation.LOGGER.info("Registering packets for Cobblemon Size Variations");
        var registrar = event.registrar(CobblemonSizeVariation.MOD_ID).versioned(PROTOCOL_VERSION);

        ModNetwork.s2cPayloads.stream().map(NeoForgePacketInfo::new).forEach(
                np -> np.registerToClient(registrar));
    }

    @Override
    public void sendPacketToPlayer(@NotNull ServerPlayer serverPlayer, @NotNull NetworkPacket<?> networkPacket) {
        serverPlayer.connection.send(networkPacket);
    }

    @Override
    public void sendToServer(@NotNull NetworkPacket<?> networkPacket) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(networkPacket);
    }
}
