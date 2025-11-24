package dev.cudzer.cobblemonsizevariation.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.events.cooking.PokeSnackSpawnPokemonEvent;
import com.cobblemon.mod.common.api.events.pokemon.FossilRevivedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShoulderMountEvent;
import com.cobblemon.mod.common.api.events.pokemon.RidePokemonEvent;
import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.data.CustomSizeDataManager;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import kotlin.Unit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.Random;

public class ModEvents {
    private static final Random random = new Random();

    public static void registerEvents(){
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, ModEvents::onCobblemonSpawn);
        CobblemonEvents.POKE_SNACK_SPAWN_POKEMON_POST.subscribe(Priority.NORMAL, ModEvents::onSnackSpawn);
        CobblemonEvents.SHOULDER_MOUNT.subscribe(Priority.NORMAL, ModEvents::onShoulderMount);
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, ModEvents::onStarterChosen);
        CobblemonEvents.FOSSIL_REVIVED.subscribe(Priority.NORMAL, ModEvents::onFossilRevived);
        CobblemonEvents.RIDE_EVENT_PRE.subscribe(Priority.NORMAL, ModEvents::onAttemptRide);
    }

    private static Unit onCobblemonSpawn(SpawnEvent<PokemonEntity> event){
        return resizer(event.getEntity().getPokemon(), null, false);
    }

    private static Unit onSnackSpawn(PokeSnackSpawnPokemonEvent.Post event){
        return resizer(event.getPokemonEntity().getPokemon(), null, false);
    }

    private static Unit onShoulderMount(ShoulderMountEvent event){
        Pokemon p = event.getPokemon();
        if(p.getScaleModifier() > ModConfig.preventShoulderMountSize){
            MutableComponent tooHeavyMessage = Component.literal("This Cobblemon is too chonky to sit on your shoulder!");
            event.getPlayer().sendSystemMessage(tooHeavyMessage);
            event.cancel();
        }
        return Unit.INSTANCE;
    }

    private static Unit onAttemptRide(RidePokemonEvent.Pre event){
        Pokemon p = event.getPokemon().getPokemon();
        MutableComponent message = Component.empty();
        boolean ridable = true;
        if(p.getScaleModifier() > ModConfig.preventRidingMaxSize){
            message = Component.literal("This Cobblemon is too big to ride!");
            ridable = false;
        }
        else if(p.getScaleModifier() < ModConfig.preventRidingMinSize){
            message = Component.literal("This Cobblemon is too small to ride!");
            ridable = false;
        }

        if(!ridable){
            event.getPlayer().sendSystemMessage(message);
            event.cancel();
        }

        return Unit.INSTANCE;
    }

    private static Unit onStarterChosen(StarterChosenEvent event){
        return resizer(event.getPokemon(), event.getPlayer(), false);
    }

    private static Unit onFossilRevived(FossilRevivedEvent event){
        return resizer(event.getPokemon(), event.getPlayer(), true);
    }

    private static boolean canModifySize(){
        return random.nextFloat() < ModConfig.sizeModificationChance;
    }

    private static Unit resizer(Pokemon pokemon, ServerPlayer player, boolean requireClientUpdate){
        if(canModifySize()){
            double sizeModifier;
            var customSize = CustomSizeDataManager.getCustomSizeFile(pokemon.getSpecies());
            if(customSize == null){
                sizeModifier = CobblemonSizeVariation.SIZER.getSize();
                pokemon.setScaleModifier((float)sizeModifier);
            }
            else{
                //use the sizes defined in the custom file, not the actual sizer
                sizeModifier = CobblemonSizeVariation.SIZER.getSize(customSize.getMinSize(), customSize.getMaxSize());
                pokemon.setScaleModifier((float)sizeModifier);
            }
            if(requireClientUpdate){
                CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(Objects.requireNonNull(player), new SizeChangedPacket(() -> pokemon, sizeModifier));
            }
        }
        return Unit.INSTANCE;
    }
}
