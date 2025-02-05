package dev.cudzer.cobblemonsizevariation;

import com.mojang.brigadier.CommandDispatcher;
import dev.cudzer.cobblemonsizevariation.command.ChangeSizeCommand;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.event.ModEvents;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CobblemonSizeVariation {
    public static final String MOD_ID = "cobblemonsizevariation";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Platform platform;
    public static ModDependencyChecker dependencyChecker;

    public static void init(Platform modPlatform) {
        platform = modPlatform;
        dependencyChecker = new ModDependencyChecker(platform);
        dependencyChecker.checkDependencies();

        ModConfig.init(platform.getConfigDirectory());
        ModEvents.registerEvents();
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        ChangeSizeCommand.registerCommand(dispatcher);
    }
}
