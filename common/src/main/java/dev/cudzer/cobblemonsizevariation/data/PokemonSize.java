package dev.cudzer.cobblemonsizevariation.data;

import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PokemonSize {
    public static Codec<PokemonSize> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.listOf().fieldOf("species").forGetter(t -> t.speciesList),
                    Codec.FLOAT.fieldOf("minSize").forGetter(t -> t.minSize),
                    Codec.FLOAT.fieldOf("maxSize").forGetter(t -> t.maxSize)
            ).apply(inst, PokemonSize::new));

    protected final List<String> speciesList;
    protected final float minSize;
    protected final float maxSize;

    private ResourceLocation jsonLocation;

    public PokemonSize(List<String> speciesList, float minSize, float maxSize) {
        this.speciesList = speciesList;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public List<String> getSpeciesList() {
        return speciesList;
    }

    public float getMinSize() {
        return minSize;
    }

    public float getMaxSize() {
        return maxSize;
    }

    public void setJsonLocation(ResourceLocation jsonLocation) {
        this.jsonLocation = jsonLocation;
    }

    public ResourceLocation getJsonLocation() {
        try{
            return jsonLocation;
        }catch (Exception e){
            CobblemonSizeVariation.LOGGER.error(String.format("Could not find json location due to %s", e));
        }
        return ResourceLocation.parse("");
    }

    public boolean isPokemonIncluded(Species species){
        return speciesList.contains(species.getName().toLowerCase());
    }
}
