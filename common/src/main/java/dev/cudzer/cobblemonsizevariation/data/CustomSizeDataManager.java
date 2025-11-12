package dev.cudzer.cobblemonsizevariation.data;

import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

/**
 * Loads and caches custom Pokémon size definitions from data/custom_sizes/.
 * Builds both a file map and a species map
 */
public class CustomSizeDataManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    protected static Map<ResourceLocation, PokemonSize> data = new HashMap<>();
    protected static List<ResourceLocation> resourceLocationList = new ArrayList<>();

    protected static Map<String, PokemonSize> speciesSizeMap = new HashMap<>();

    public CustomSizeDataManager() {
        super(GSON, CobblemonSizeVariation.cobblemonSizeResource("custom_sizes").getPath());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        CobblemonSizeVariation.LOGGER.info("Loading custom pokemon sizes...");

        Map<ResourceLocation, PokemonSize> newMap = new HashMap<>();
        List<ResourceLocation> newResourceLocationList = new ArrayList<>();
        Map<String, PokemonSize> newSpeciesMap = new HashMap<>();

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

                        for (String speciesName : pokemonSize.speciesList) {
                            if (speciesName == null || speciesName.isBlank()) continue;
                            newSpeciesMap.put(speciesName.toLowerCase(Locale.ROOT), pokemonSize);
                        }
                    })
                    .ifError( partial -> CobblemonSizeVariation.LOGGER.error("Failed to parse json data for {} due to {}", key, partial.message()));
        }
        resourceLocationList = newResourceLocationList;
        data = newMap;
        speciesSizeMap = newSpeciesMap;
        CobblemonSizeVariation.LOGGER.info("Loaded {} custom size files", data.size());
    }

    public static PokemonSize getCustomSizeFile(Species species) {
        if (species == null) return null;
        return speciesSizeMap.get(species.getName().toLowerCase(Locale.ROOT));
    }
}
