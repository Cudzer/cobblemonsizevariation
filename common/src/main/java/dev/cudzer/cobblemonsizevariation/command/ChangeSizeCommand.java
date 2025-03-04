package dev.cudzer.cobblemonsizevariation.command;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ConfigKey;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class ChangeSizeCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("pokesizer")
                .then(Commands.argument("player", EntityArgument.player()).requires( src -> src.hasPermission(ModConfig.getPermission(ConfigKey.POKESIZER_PERM_NAME)))
                        .then(Commands.argument("member", StringArgumentType.string()).suggests((ctx, sb) -> SharedSuggestionProvider.suggest(getPartyMemberNames(ctx), sb))
                                .then(Commands.argument("size",DoubleArgumentType.doubleArg(CobblemonSizeVariation.SIZER.getMinSizeModifier(), CobblemonSizeVariation.SIZER.getMaxSizeModifier()))
                        .executes(ChangeSizeCommand::runResizer)))));

        dispatcher.register(Commands.literal("pokesizer")
                .then(Commands.literal("self").requires( src -> src.hasPermission(ModConfig.getPermission(ConfigKey.POKESIZER_SELF_PERM_NAME)))
                        .then(Commands.argument("member", StringArgumentType.string()).suggests((ctx, sb) -> SharedSuggestionProvider.suggest(getSelfPartyMemberNames(ctx), sb))
                                .then(Commands.argument("size",DoubleArgumentType.doubleArg(CobblemonSizeVariation.SIZER.getMinSizeModifier(), CobblemonSizeVariation.SIZER.getMaxSizeModifier()))
                                        .executes(ChangeSizeCommand::runSelfResizer)))));
    }

    private static int runResizer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            String partyMember = StringArgumentType.getString(context, "member");
            double sizeModifier = DoubleArgumentType.getDouble(context, "size");

            Pokemon targetPokemon = party.toGappyList().stream().filter(p -> p.getSpecies().getName().equals(partyMember)).findFirst().orElse(null);
            if(targetPokemon != null){
                targetPokemon.setScaleModifier((float)sizeModifier);
                context.getSource().sendSuccess(() -> Component.literal(String.format("The size of %s's %s was changed",targetPlayer.getName().getString(), partyMember)), true);
                CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(targetPlayer, new SizeChangedPacket(() -> targetPokemon, sizeModifier));
                return 0;
            }
            else {
                context.getSource().sendFailure((Component.literal(String.format("%s doesn't have a %s",targetPlayer.getName().getString(), partyMember))));
                return  -1;
            }
        }
        return -1;
    }

    private static int runSelfResizer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = context.getSource().getPlayer();

            if(targetPlayer == null) {
                context.getSource().sendFailure((Component.literal("You must be a player to run this command")));
                return  -1;
            }
            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            String partyMember = StringArgumentType.getString(context, "member");
            double sizeModifier = DoubleArgumentType.getDouble(context, "size");

            Pokemon targetPokemon = party.toGappyList().stream().filter(p -> p.getSpecies().getName().equals(partyMember)).findFirst().orElse(null);
            if(targetPokemon != null){
                targetPokemon.setScaleModifier((float)sizeModifier);
                context.getSource().sendSuccess(() -> Component.literal(String.format("The size of %s's %s was changed",targetPlayer.getName().getString(), partyMember)), true);
                CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(targetPlayer, new SizeChangedPacket(() -> targetPokemon, sizeModifier));
                return 0;
            }
            else {
                context.getSource().sendFailure((Component.literal(String.format("%s doesn't have a %s",targetPlayer.getName().getString(), partyMember))));
                return  -1;
            }
        }
        return -1;
    }

    private static Set<String> getPartyMemberNames(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
        PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

        Set<String> partyMembers = new HashSet<>();

        party.forEach(p -> partyMembers.add(p.getSpecies().getName()));
        return partyMembers;
    }

    private static Set<String> getSelfPartyMemberNames(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = context.getSource().getPlayer();
        if(targetPlayer != null){
            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            Set<String> partyMembers = new HashSet<>();

            party.forEach(p -> partyMembers.add(p.getSpecies().getName()));
            return partyMembers;
        }
        return new HashSet<>();
    }
}

