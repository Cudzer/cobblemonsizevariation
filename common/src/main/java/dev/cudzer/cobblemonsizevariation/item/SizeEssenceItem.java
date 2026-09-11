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

    /**
     * Which end of Cobblemon's intrinsic size range a SET essence aims at.
     *
     * The bound itself is deliberately not stored here. Items are built during
     * registration, before Cobblemon has read its config, and the range can be
     * edited afterwards; keeping the intent rather than the number means the
     * value is read when the essence is used and always matches the config.
     */
    public enum SizeTarget{
        MINIMUM,
        NORMAL,
        MAXIMUM
    }

    private final float sizeChange;
    private final SizeTarget sizeTarget;
    private final SizeModification sizeModification;
    private final SizeModificationType sizeModificationType;

    /**
     * An essence that adds to the current size.
     */
    public SizeEssenceItem(Properties properties, float size, SizeModificationType sizeModificationType) {
        super(properties);

        this.sizeChange = size;
        this.sizeTarget = null;
        this.sizeModification = SizeModification.ADDITION;
        this.sizeModificationType = sizeModificationType;
    }

    /**
     * An essence that sets the size to one end of the configured range.
     */
    public SizeEssenceItem(Properties properties, SizeTarget sizeTarget) {
        super(properties);

        this.sizeChange = 0.0f;
        this.sizeTarget = sizeTarget;
        this.sizeModification = SizeModification.SET;
        this.sizeModificationType = SizeModificationType.SET;
    }

    public float getSizeChange() {
        return sizeChange;
    }

    public SizeTarget getSizeTarget() {
        return sizeTarget;
    }

    public SizeModification getSizeModification() {
        return sizeModification;
    }

    public SizeModificationType getSizeModificationType() {
        return sizeModificationType;
    }
}
