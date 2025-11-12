package dev.cudzer.cobblemonsizevariation.fabric.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.cudzer.cobblemonsizevariation.utils.SizeItemInteractions;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.Set;

public class EntityInteractEvents {
    private static final Set<Integer> handledEntitiesThisTick = new HashSet<>();
    private static int lastGameTick = -1;

    public static void register() {
        UseEntityCallback.EVENT.register(EntityInteractEvents::onEntityUse);
    }

    private static InteractionResult onEntityUse(Player player, Level world, InteractionHand hand,
                                                 net.minecraft.world.entity.Entity entity, EntityHitResult hitResult) {
        if (world.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!(entity instanceof PokemonEntity living)) return InteractionResult.PASS;

        int currentTick = (int) world.getGameTime();
        int id = entity.getId();

        if (currentTick != lastGameTick) {
            handledEntitiesThisTick.clear();
            lastGameTick = currentTick;
        }

        if (handledEntitiesThisTick.contains(id)) {
            return InteractionResult.PASS;
        }
        handledEntitiesThisTick.add(id);

        boolean result = SizeItemInteractions.handleEntityInteract(player, world, hand, living);
        return result ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}
