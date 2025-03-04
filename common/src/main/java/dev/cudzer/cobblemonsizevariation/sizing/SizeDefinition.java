package dev.cudzer.cobblemonsizevariation.sizing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cudzer.cobblemonsizevariation.config.Size;

import java.util.List;

public class SizeDefinition {
    public static Codec<SizeDefinition> CODEC = RecordCodecBuilder.create( inst -> inst
            .group(
                    Codec.STRING.fieldOf("name").forGetter(n -> n.name),
                    Codec.STRING.fieldOf("minSizeModifier").forGetter(s -> s.minSizeModifier),
                    Codec.STRING.fieldOf("maxSizeModifier").forGetter(s -> s.maxSizeModifier),
                    Size.CODEC.listOf().fieldOf("sizes").forGetter(s -> s.sizes)
            ).apply(inst, SizeDefinition::new));

    protected String name;
    protected String minSizeModifier;
    protected String maxSizeModifier;
    protected List<Size> sizes;

    public SizeDefinition(String name, String minSizeModifier, String maxSizeModifier, List<Size> sizes){
        this.name = name;
        this.minSizeModifier = minSizeModifier;
        this.maxSizeModifier = maxSizeModifier;
        this.sizes = sizes;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public String getMinSizeModifier() {
        return minSizeModifier;
    }

    public String getMaxSizeModifier() {
        return maxSizeModifier;
    }
}
