package dev.cudzer.cobblemonsizevariation.sizing;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.sizing.algorithms.BasicSizer;
import dev.cudzer.cobblemonsizevariation.sizing.algorithms.GenIXSizer;
import dev.cudzer.cobblemonsizevariation.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SizeDataManager {

    private final List<JsonElement> sizeDefinitionJsons = new ArrayList<>();

    public  List<SizeDefinition> sizeDefinitions = new ArrayList<>();

    public SizeDataManager() {

    }

    public void init() {
        CobblemonSizeVariation.LOGGER.info("Loading sizing algorithms...");

        List<SizeDefinition> newDefinitions = new ArrayList<>();

        sizeDefinitionJsons.add(BasicSizer.createConfig(FileUtils.getSizeFile("basic.json")));
        sizeDefinitionJsons.add(GenIXSizer.createConfig(FileUtils.getSizeFile("gen9.json")));

        for(JsonElement element : sizeDefinitionJsons){
            SizeDefinition.CODEC.decode(JsonOps.INSTANCE, element)
                    .ifSuccess( result -> {
                        SizeDefinition definition = result.getFirst();
                        newDefinitions.add(definition);
                    })
                    .ifError( partial -> CobblemonSizeVariation.LOGGER.error("Failed to parse json data when loading size files. Error: {}", partial.message()));
        }

        sizeDefinitions.addAll(newDefinitions);
        newDefinitions.clear();

        CobblemonSizeVariation.LOGGER.info("Loaded {} size definitions.", sizeDefinitions.size());
    }

    public SizeDefinition getDefinition(String name) {
        Objects.requireNonNull(name, "Definition name cannot be null");
        SizeDefinition def = findByName(name);

        if (def != null) return def;

        CobblemonSizeVariation.LOGGER.warn("[CSV] Unknown SizeDefinition '{}'. Falling back to 'basic'.", name);
        SizeDefinition fallback = findByName("basic");
        if (fallback == null)
            throw new IllegalStateException("[CSV] No 'basic' SizeDefinition found. Mod cannot continue safely.");

        return fallback;
    }

    private SizeDefinition findByName(String name) {
        return sizeDefinitions.stream()
                .filter(d -> d.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
