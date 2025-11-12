package dev.cudzer.cobblemonsizevariation.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Size(String name, String min, String max, String color) {
    public static Codec<Size> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("name").forGetter(n -> n.name),
                    Codec.STRING.fieldOf("min").forGetter(m -> m.min),
                    Codec.STRING.fieldOf("max").forGetter(m -> m.max),
                    Codec.STRING.fieldOf("color").forGetter(c -> c.color)
            ).apply(inst, Size::new));


}
