package dev.cudzer.cobblemonsizevariation.neoforge.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.cudzer.cobblemonsizevariation.utils.SizeItemInteractions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class EntityInteractEvents {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof PokemonEntity living) {
            boolean result = SizeItemInteractions.handleEntityInteract(
                    event.getEntity(), event.getLevel(), event.getHand(), living
            );
            if (result) event.setCanceled(true);
        }
    }
}