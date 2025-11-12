package dev.cudzer.cobblemonsizevariation.network.handler.client;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class SizeChangeHandler implements ClientNetworkPacketHandler<SizeChangedPacket> {
    @Override
    public void handle(@NotNull SizeChangedPacket sizeChangedPacket, @NotNull Minecraft minecraft) {
        minecraft.execute(() -> {
            sizeChangedPacket.applyToPokemon();
            Pokemon pokemon = sizeChangedPacket.getPokemon().invoke();

            var entity = pokemon.getEntity();
            if (entity != null) {
                Pokemon entityPokemon = entity.getPokemon();

                float newScale = pokemon.getScaleModifier();
                entityPokemon.setScaleModifier(newScale);
                entity.refreshDimensions();
            }
        });
    }
}
