package dev.cudzer.cobblemonsizevariation.sizing.algorithms;

import dev.cudzer.cobblemonsizevariation.config.Size;

public interface ISizer {
    float getSize();
    Size getSizeInformation(float size);
    float getMinSizeModifier();
    float getMaxSizeModifier();
}
