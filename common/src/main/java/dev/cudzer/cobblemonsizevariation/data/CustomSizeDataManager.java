package dev.cudzer.cobblemonsizevariation.data;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSizeDataManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    protected static Map<ResourceLocation, PokemonSize> data = new HashMap<>();
    protected static List<ResourceLocation> resourceLocationList = new ArrayList<>();

    public CustomSizeDataManager() {
        super(GSON, CobblemonSizeVariation.cobblemonSizeResource("custom_sizes").getPath());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        CobblemonSizeVariation.LOGGER.info("Loading custom pokemon sizes...");

        Map<ResourceLocation, PokemonSize> newMap = new HashMap<>();
        List<ResourceLocation> newResourceLocationList = new ArrayList<>();

        data.clear();
        resourceLocationList.clear();

        for(Map.Entry<ResourceLocation, JsonElement> entry : json.entrySet()){
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();

            PokemonSize.CODEC.decode(JsonOps.INSTANCE, element)
                    .ifSuccess( result -> {
                        PokemonSize pokemonSize = result.getFirst();
                        newMap.put(key, pokemonSize);
                        pokemonSize.setJsonLocation(key);
                        newResourceLocationList.add(key);
                    })
                    .ifError( partial -> {
                       CobblemonSizeVariation.LOGGER.error(String.format("Failed to parse json data for %s due to %s", key, partial.message()));
                    });
        }
        resourceLocationList = newResourceLocationList;
        data = newMap;
        CobblemonSizeVariation.LOGGER.info(String.format("Loaded %s custom size files", data.size()));
    }

    public static PokemonSize getCustomSizeFile(Species pokemon){
        for(var ps : data.values()){
            if(ps.isPokemonIncluded(pokemon)){
                return ps;
            }
        }
        return null;
    }
}
