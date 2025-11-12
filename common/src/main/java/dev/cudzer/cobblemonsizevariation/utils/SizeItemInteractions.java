package dev.cudzer.cobblemonsizevariation.utils;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.item.SizeEssenceItem;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SizeItemInteractions {

    private static final ResourceLocation SIZE_CHANGED_SOUND = ResourceLocation.fromNamespaceAndPath(CobblemonSizeVariation.MOD_ID, "size_changed");

    public static boolean handleEntityInteract(Player player, Level level, InteractionHand hand, PokemonEntity target) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof SizeEssenceItem sizeItem)) return false;
        if(target.getPokemon().getOwnerUUID() == null) return false;
        if(!target.getPokemon().getOwnerUUID().equals(player.getUUID())) return false;

        if (level.isClientSide) return true;
        if(target.getPokemon().getScaleModifier() >= CobblemonSizeVariation.SIZER.getMaxSizeModifier() &&
                sizeItem.getSizeModificationType() == SizeEssenceItem.SizeModificationType.GROW){
            player.displayClientMessage(
                    Component.literal(getPokemonName(target) + " is at maximum size!"),
                    true
            );
            return false;
        }
        if(target.getPokemon().getScaleModifier() <= CobblemonSizeVariation.SIZER.getMinSizeModifier() &&
            sizeItem.getSizeModificationType() == SizeEssenceItem.SizeModificationType.SHRINK){
            player.displayClientMessage(
                    Component.literal(getPokemonName(target) + " is at minimum size!"),
                    true
            );
            return false;
        }

        if(sizeItem.getSizeModification().equals(SizeEssenceItem.SizeModification.SET)){
            setPokemonSize((ServerPlayer) player, target, sizeItem.getSizeChange());
            player.displayClientMessage(
                    Component.literal(getPokemonName(target) + " is now size " + String.format("%.2f", sizeItem.getSizeChange())),
                    true
            );
        }
        else {
            float currentSize = getPokemonSize(target);
            float newSize = Math.max(0.1f, Math.min(10.0f, currentSize + sizeItem.getSizeChange()));
            setPokemonSize((ServerPlayer) player,target, newSize);
            player.displayClientMessage(
                    Component.literal(getPokemonName(target) + " is now size " + String.format("%.2f", newSize)),
                    true
            );
        }
        if(!player.isCreative()){
            stack.shrink(1);
        }
        SoundEvent sound = SoundEvent.createFixedRangeEvent(SIZE_CHANGED_SOUND, 64.0f);
        target.level().playSound(target, target.blockPosition(), sound, SoundSource.NEUTRAL, 2.0f, 1.0f);
        return true;
    }

    private static float getPokemonSize(PokemonEntity pokemon) {
        return pokemon.getPokemon().getScaleModifier();
    }
    private static void setPokemonSize(ServerPlayer player, PokemonEntity pokemonEntity, float newSize) {
        if (!pokemonEntity.level().isClientSide) {
            pokemonEntity.getPokemon().setScaleModifier(newSize);
            CobblemonSizeVariation.platform.getNetworkManager()
                    .sendPacketToPlayer(Objects.requireNonNull(player), new SizeChangedPacket(pokemonEntity::getPokemon, (double)newSize));
        }
    }
    private static String getPokemonName(PokemonEntity pokemon) {
        return pokemon.getPokemon().getSpecies().getName();
    }
}