package dev.cudzer.cobblemonsizevariation.mixin;

import com.google.gson.JsonObject;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * Prevents recipe JSONs from being loaded when enableRecipes = false.
 */
@Mixin(RecipeManager.class)
public class RecipeBlockerMixin{
    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("HEAD")
    )
    private void csv$disableRecipes(Map<ResourceLocation, JsonObject> map,
                                    ResourceManager manager,
                                    ProfilerFiller profiler,
                                    CallbackInfo ci) {
        if (!ModConfig.enableEssenceRecipes) {
            map.keySet().removeIf(id -> id.getNamespace().equals("cobblemonsizevariation"));

            CobblemonSizeVariation.LOGGER.info("[CSV] Removed all cobblemonsizevariation recipes because enableRecipes=false.");
        }
    }
}
