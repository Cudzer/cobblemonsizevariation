package dev.cudzer.cobblemonsizevariation.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.events.cooking.PokeSnackSpawnPokemonEvent;
import com.cobblemon.mod.common.api.events.pokemon.FossilRevivedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShoulderMountEvent;
import com.cobblemon.mod.common.api.events.pokemon.RidePokemonEvent;
import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.data.CustomSizeDataManager;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ModEvents {
    private static final Random random = new Random();
    private static final Map<UUID, Map<UUID, Float>> pvpBattleOriginalScales = new ConcurrentHashMap<>();

    public static void registerEvents(){
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, ModEvents::onCobblemonSpawn);
        CobblemonEvents.POKE_SNACK_SPAWN_POKEMON_POST.subscribe(Priority.NORMAL, ModEvents::onSnackSpawn);
        CobblemonEvents.SHOULDER_MOUNT.subscribe(Priority.NORMAL, ModEvents::onShoulderMount);
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, ModEvents::onStarterChosen);
        CobblemonEvents.FOSSIL_REVIVED.subscribe(Priority.NORMAL, ModEvents::onFossilRevived);
        CobblemonEvents.RIDE_EVENT_PRE.subscribe(Priority.NORMAL, ModEvents::onAttemptRide);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, ModEvents::onBattleStarted);
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ModEvents::onBattleVictory);
        CobblemonEvents.BATTLE_FLED.subscribe(Priority.NORMAL, ModEvents::onBattleFled);
    }

    private static void onCobblemonSpawn(SpawnEvent<PokemonEntity> event){
        resizer(event.getEntity().getPokemon(), null, false);
    }

    private static void onSnackSpawn(PokeSnackSpawnPokemonEvent.Post event){
        resizer(event.getPokemonEntity().getPokemon(), null, false);
    }

    private static void onShoulderMount(ShoulderMountEvent event){
        Pokemon p = event.getPokemon();
        if(p.getScaleModifier() > ModConfig.preventShoulderMountSize){
            MutableComponent tooHeavyMessage = Component.literal("This Cobblemon is too chonky to sit on your shoulder!");
            event.getPlayer().sendSystemMessage(tooHeavyMessage);
            event.cancel();
        }
    }

    private static void onAttemptRide(RidePokemonEvent.Pre event){
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
    }

    private static void onStarterChosen(StarterChosenEvent event){
        resizer(event.getPokemon(), event.getPlayer(), false);
    }

    private static void onFossilRevived(FossilRevivedEvent event){
        resizer(event.getPokemon(), event.getPlayer(), true);
    }

    private static void onBattleStarted(BattleStartedEvent.Post event){
        if(!ModConfig.disableResizingInPvP) return;

        PokemonBattle battle = event.getBattle();
        if(!battle.isPvP()) return;

        Map<UUID, Float> originalScales = new HashMap<>();
        for(var actor : battle.getActors()){
            for(var battlePokemon : actor.getPokemonList()){
                Pokemon p = battlePokemon.getEffectedPokemon();
                UUID pokemonUuid = p.getUuid();

                originalScales.putIfAbsent(pokemonUuid, p.getScaleModifier());
                if(p.getScaleModifier() != 1.0f){
                    p.setScaleModifier(1.0f);
                    syncScaleToBattlePlayers(battle, p, 1.0f);
                }
            }
        }

        if(!originalScales.isEmpty()){
            pvpBattleOriginalScales.put(battle.getBattleId(), originalScales);
        }
    }

    private static void onBattleVictory(BattleVictoryEvent event){
        restoreBattleScales(event.getBattle());
    }

    private static void onBattleFled(BattleFledEvent event){
        restoreBattleScales(event.getBattle());
    }

    private static void restoreBattleScales(PokemonBattle battle){
        Map<UUID, Float> originalScales = pvpBattleOriginalScales.remove(battle.getBattleId());
        if(originalScales == null || originalScales.isEmpty()) return;

        for(var actor : battle.getActors()){
            for(var battlePokemon : actor.getPokemonList()){
                Pokemon p = battlePokemon.getEffectedPokemon();
                Float originalScale = originalScales.get(p.getUuid());
                if(originalScale != null && p.getScaleModifier() != originalScale){
                    p.setScaleModifier(originalScale);
                    syncScaleToBattlePlayers(battle, p, originalScale);
                }
            }
        }
    }

    private static void syncScaleToBattlePlayers(PokemonBattle battle, Pokemon pokemon, double scale){
        for(ServerPlayer battlePlayer : battle.getPlayers()){
            CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(
                    battlePlayer,
                    new SizeChangedPacket(() -> pokemon, scale)
            );
        }
    }

    private static boolean canModifySize(){
        return random.nextFloat() < ModConfig.sizeModificationChance;
    }

    private static void resizer(Pokemon pokemon, ServerPlayer player, boolean requireClientUpdate){
        if(player != null && ModConfig.disableResizingInPvP){
            PokemonBattle currentBattle = BattleRegistry.getBattleByParticipatingPlayer(player);
            if(currentBattle != null && currentBattle.isPvP()){
                return;
            }
        }

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
    }
}
