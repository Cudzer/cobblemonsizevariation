package dev.cudzer.cobblemonsizevariation.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import static dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> TINY_ESSENCE = ITEMS.register("tiny_essence", () ->
            new SizeEssenceItem(new Item.Properties(), SizeEssenceItem.SizeTarget.MINIMUM));

    public static final RegistrySupplier<Item> NORMAL_ESSENCE = ITEMS.register("normal_essence", () ->
            new SizeEssenceItem(new Item.Properties(), SizeEssenceItem.SizeTarget.NORMAL));

    public static final RegistrySupplier<Item> HUGE_ESSENCE = ITEMS.register("huge_essence", () ->
            new SizeEssenceItem(new Item.Properties(), SizeEssenceItem.SizeTarget.MAXIMUM));

    public static final RegistrySupplier<Item> SHRINK_ESSENCE = ITEMS.register("shrink_essence", () ->
            new SizeEssenceItem(new Item.Properties(), -0.1f, SizeEssenceItem.SizeModificationType.SHRINK));

    public static final RegistrySupplier<Item> GROWTH_ESSENCE = ITEMS.register("growth_essence", () ->
            new SizeEssenceItem(new Item.Properties(), 0.1f, SizeEssenceItem.SizeModificationType.GROW));


    public static void register() {
        ITEMS.register();
    }
}
