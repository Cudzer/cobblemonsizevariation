package dev.cudzer.cobblemonsizevariation.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> SIZE_VARIATION_TAB =
            CREATIVE_MODE_TAB.register("size_variation_tab", () ->
                    CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                            .title(Component.translatable("itemGroup." + MOD_ID))
                            .icon(() -> new ItemStack(ModItems.NORMAL_ESSENCE.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.TINY_ESSENCE.get());
                                output.accept(ModItems.NORMAL_ESSENCE.get());
                                output.accept(ModItems.HUGE_ESSENCE.get());
                                output.accept(ModItems.SHRINK_ESSENCE.get());
                                output.accept(ModItems.GROWTH_ESSENCE.get());
                            })
                            .build()
            );

    public static void register() {
        CREATIVE_MODE_TAB.register();
    }
}
