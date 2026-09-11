package dev.cudzer.cobblemonsizevariation.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.PokemonSizeCategory;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.item.SizeEssenceItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SizeItemInteractions {

    private static final ResourceLocation SIZE_CHANGED_SOUND = ResourceLocation.fromNamespaceAndPath(CobblemonSizeVariation.MOD_ID, "size_changed");

    /**
     * Cobblemon owns the range. Reading it here, rather than caching it, keeps
     * the essences in step with pokemonIntrinsicSizeMin/Max however it is edited.
     */
    private static float minSize() {
        return Cobblemon.INSTANCE.getConfig().getPokemonIntrinsicSizeMin();
    }

    private static float maxSize() {
        return Cobblemon.INSTANCE.getConfig().getPokemonIntrinsicSizeMax();
    }

    public static boolean handleEntityInteract(Player player, Level level, InteractionHand hand, PokemonEntity target) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof SizeEssenceItem sizeItem)) return false;
        if (target.getPokemon().getOwnerUUID() == null) return false;
        if (!target.getPokemon().getOwnerUUID().equals(player.getUUID())) return false;

        if (level.isClientSide) return true;

        float currentSize = target.getPokemon().getScaleModifier();

        if (currentSize >= maxSize() && sizeItem.getSizeModificationType() == SizeEssenceItem.SizeModificationType.GROW) {
            player.displayClientMessage(
                    Component.translatable("message.cobblemonsizevariation.at_maximum", getPokemonName(target)),
                    true
            );
            return false;
        }
        if (currentSize <= minSize() && sizeItem.getSizeModificationType() == SizeEssenceItem.SizeModificationType.SHRINK) {
            player.displayClientMessage(
                    Component.translatable("message.cobblemonsizevariation.at_minimum", getPokemonName(target)),
                    true
            );
            return false;
        }

        float newSize;
        if (sizeItem.getSizeModification() == SizeEssenceItem.SizeModification.SET) {
            newSize = switch (sizeItem.getSizeTarget()) {
                case MINIMUM -> minSize();
                case MAXIMUM -> maxSize();
                case NORMAL -> 1.0f;
            };
        } else {
            newSize = Math.max(minSize(), Math.min(maxSize(), currentSize + sizeItem.getSizeChange()));
        }

        // setScaleModifier syncs to the client on its own since Cobblemon 1.8, so
        // there is no packet to send from here.
        target.getPokemon().setScaleModifier(newSize);

        player.displayClientMessage(
                Component.translatable("message.cobblemonsizevariation.size_changed",
                        getPokemonName(target), categoryName(target.getPokemon().getSizeCategory())),
                true
        );

        if (!player.isCreative()) {
            stack.shrink(1);
        }
        SoundEvent sound = SoundEvent.createFixedRangeEvent(SIZE_CHANGED_SOUND, 64.0f);
        target.level().playSound(target, target.blockPosition(), sound, SoundSource.NEUTRAL, 2.0f, 1.0f);
        return true;
    }

    /**
     * The bracket names come from Cobblemon too, so they follow the player's
     * language and stay correct if the range is retuned.
     */
    private static Component categoryName(PokemonSizeCategory category) {
        return Component.translatable(PokemonSizeCategory.Companion.translationKey(category));
    }

    private static String getPokemonName(PokemonEntity pokemon) {
        return pokemon.getPokemon().getSpecies().getName();
    }
}
