package dev.cudzer.cobblemonsizevariation;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.cudzer.cobblemonsizevariation.command.ChangeSizeCommand;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.data.CustomSizeDataManager;
import dev.cudzer.cobblemonsizevariation.event.ModEvents;
import dev.cudzer.cobblemonsizevariation.sizing.SizeDataManager;
import dev.cudzer.cobblemonsizevariation.sizing.algorithms.BasicSizer;
import dev.cudzer.cobblemonsizevariation.sizing.algorithms.GenIXSizer;
import dev.cudzer.cobblemonsizevariation.sizing.algorithms.ISizer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CobblemonSizeVariation {
    public static final String MOD_ID = "cobblemonsizevariation";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SizeDataManager sizeDataManager;
    public static ISizer SIZER;

    public static Platform platform;
    public static ModDependencyChecker dependencyChecker;

    public static ResourceLocation cobblemonSizeResource(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init(Platform modPlatform) {
        platform = modPlatform;
        dependencyChecker = new ModDependencyChecker(platform);
        dependencyChecker.checkDependencies();

        ModConfig.init(platform.getConfigDirectory());
        sizeDataManager = new SizeDataManager();
        sizeDataManager.init();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new CustomSizeDataManager(), cobblemonSizeResource("custom_sizes"));

        SIZER = getSizer();

        ModEvents.registerEvents();
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        ChangeSizeCommand.registerCommand(dispatcher);
    }

    private static ISizer getSizer(){
        String sizerName = ModConfig.sizingAlgorithm;
        switch (ModConfig.sizingAlgorithm){
            case "gen9":
                return new GenIXSizer(CobblemonSizeVariation.sizeDataManager.getDefinition(sizerName));
            default:
                return new BasicSizer(CobblemonSizeVariation.sizeDataManager.getDefinition(sizerName));
        }
    }
}

