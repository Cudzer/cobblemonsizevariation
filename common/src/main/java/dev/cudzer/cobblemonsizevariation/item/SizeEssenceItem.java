package dev.cudzer.cobblemonsizevariation.item;

import net.minecraft.world.item.Item;

public class SizeEssenceItem extends Item {
    public enum SizeModification{
        SET,
        ADDITION
    }

    public enum SizeModificationType{
        SHRINK,
        GROW,
        SET
    }

    private final float sizeChange;
    private final SizeModification sizeModification;
    private final SizeModificationType sizeModificationType;


    public SizeEssenceItem(Properties properties, float size, SizeModification sizeModification, SizeModificationType sizeModificationType) {
        super(properties);

        this.sizeChange = size;
        this.sizeModification = sizeModification;
        this.sizeModificationType = sizeModificationType;
    }

    public float getSizeChange() {
        return sizeChange;
    }

    public SizeModification getSizeModification() {
        return sizeModification;
    }

    public SizeModificationType getSizeModificationType() {
        return sizeModificationType;
    }
}