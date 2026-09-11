package dev.cudzer.cobblemonsizevariation.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShoulderMountEvent;
import com.cobblemon.mod.common.api.events.pokemon.RidePokemonEvent;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import net.minecraft.network.chat.Component;

/**
 * The two limits Cobblemon does not have of its own.
 *
 * Rolling a size on spawn used to live here as well. Cobblemon 1.8 does that
 * itself, in Pokemon.initializeScale, so the mod no longer competes for the
 * field: it only reacts to sizes that are already set.
 *
 * There is no minimum-to-ride check either, because Cobblemon has one:
 * minimumRidingScale, enforced in PokemonEntity.tryRidingPokemon.
 */
public class ModEvents {

    public static void registerEvents(){
        CobblemonEvents.SHOULDER_MOUNT.subscribe(Priority.NORMAL, ModEvents::onShoulderMount);
        CobblemonEvents.RIDE_EVENT_PRE.subscribe(Priority.NORMAL, ModEvents::onAttemptRide);
    }

    private static void onShoulderMount(ShoulderMountEvent event){
        Pokemon p = event.getPokemon();
        if(p.getScaleModifier() > ModConfig.preventShoulderMountSize){
            event.getPlayer().sendSystemMessage(
                    Component.translatable("message.cobblemonsizevariation.too_chonky"));
            event.cancel();
        }
    }

    private static void onAttemptRide(RidePokemonEvent.Pre event){
        Pokemon p = event.getPokemon().getPokemon();
        if(p.getScaleModifier() > ModConfig.preventRidingMaxSize){
            event.getPlayer().sendSystemMessage(
                    Component.translatable("message.cobblemonsizevariation.too_big_to_ride"));
            event.cancel();
        }
    }
}
