package dev.cudzer.cobblemonsizevariation.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Size {
    public static Codec<Size> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("name").forGetter(n -> n.name),
                    Codec.STRING.fieldOf("min").forGetter(m -> m.min),
                    Codec.STRING.fieldOf("max").forGetter(m -> m.max),
                    Codec.STRING.fieldOf("color").forGetter(c -> c.color)
            ).apply(inst, Size::new));


    private final String name;
    private final String min;
    private final String max;
    private final String color;

    public Size(String name, String min, String max, String color){
        this.name = name;
        this.min = min;
        this.max = max;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor(){
        return color;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }
}
