package dev.cudzer.cobblemonsizevariation.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShoulderMountEvent;
import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import kotlin.Unit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Random;

public class ModEvents {
    private static final Random random = new Random();

    public static void registerEvents(){
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, ModEvents::onCobblemonSpawn);
        CobblemonEvents.SHOULDER_MOUNT.subscribe(Priority.NORMAL, ModEvents::onShoulderMount);
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, ModEvents::onStarterChosen);
    }

    private static Unit onCobblemonSpawn(SpawnEvent<PokemonEntity> event){
        if(canModifySize()){
            PokemonEntity entityToSpawn = event.getEntity();
            Pokemon p = entityToSpawn.getPokemon();
            p.setScaleModifier(generateScaleModifier());
        }
        return Unit.INSTANCE;
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

    private static Unit onStarterChosen(StarterChosenEvent event){
        if(canModifySize()){
            Pokemon starter = event.getPokemon();
            starter.setScaleModifier(generateScaleModifier());
        }
        return Unit.INSTANCE;
    }

    private static float generateScaleModifier(){
        return random.nextFloat() * (
                ModConfig.maxSizeMultiplier - ModConfig.minSizeMultiplier)
                + ModConfig.minSizeMultiplier;
    }

    private static boolean canModifySize(){
        return random.nextFloat() < ModConfig.sizeModificationChance;
    }
}
