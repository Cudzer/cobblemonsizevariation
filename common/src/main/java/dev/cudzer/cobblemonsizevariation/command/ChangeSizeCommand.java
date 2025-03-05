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
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ChangeSizeCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                literal("pokesizer")
                        .then(
                                argument("player", EntityArgument.player()).requires(src -> src.hasPermission(ModConfig.getPermission(ConfigKey.POKESIZER_PERM_NAME)))
                                        .executes(ChangeSizeCommand::runResizeAll)
                                        .then(
                                                argument("member", StringArgumentType.string())
                                                        .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(getPartyMemberNames(ctx), sb))
                                                        .executes(ChangeSizeCommand::runRandomResizer)
                                                        .then(
                                                                argument("size", DoubleArgumentType.doubleArg(CobblemonSizeVariation.SIZER.getMinSizeModifier(), CobblemonSizeVariation.SIZER.getMaxSizeModifier()))
                                                                        .executes(ChangeSizeCommand::runResizer)
                                                        )
                                        )
                        )
                        .then(
                                literal("self").requires(src -> src.hasPermission(ModConfig.getPermission(ConfigKey.POKESIZER_SELF_PERM_NAME)))
                                        .executes(ChangeSizeCommand::runResizeSelfAll)
                                        .then(
                                                argument("member", StringArgumentType.string())
                                                        .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(getSelfPartyMemberNames(ctx), sb))
                                                        .executes(ChangeSizeCommand::runRandomSelfResizer)
                                                        .then(
                                                                argument("size", DoubleArgumentType.doubleArg(CobblemonSizeVariation.SIZER.getMinSizeModifier(), CobblemonSizeVariation.SIZER.getMaxSizeModifier()))
                                                                        .executes(ChangeSizeCommand::runSelfResizer)
                                                        )
                                        )
                        )
        );
    }

    private static int runResizeAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            if(party.size() != 0){
                for (Pokemon targetPokemon : party.toGappyList()){
                    if(targetPokemon == null) continue;
                    float sizeModifier = CobblemonSizeVariation.SIZER.getSize();
                    targetPokemon.setScaleModifier(sizeModifier);

                    CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(targetPlayer, new SizeChangedPacket(() -> targetPokemon, (double)sizeModifier));
                }
                context.getSource().sendSuccess(() -> Component.literal(String.format("The size of %s's team has been randomized",targetPlayer.getName().getString())), true);
                return 0;
            }
            else {
                context.getSource().sendFailure((Component.literal(String.format("%s doesn't have any Pokemon!",targetPlayer.getName().getString()))));
                return  -1;
            }
        }
        return -1;
    }

    private static int runResizeSelfAll(CommandContext<CommandSourceStack> context) {

        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = context.getSource().getPlayer();

            if(targetPlayer == null) {
                context.getSource().sendFailure((Component.literal("You must be a player to run this command")));
                return  -1;
            }

            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            if(party.size() != 0){
                for (Pokemon targetPokemon : party.toGappyList()){
                    if(targetPokemon == null) continue;
                    float sizeModifier = CobblemonSizeVariation.SIZER.getSize();
                    targetPokemon.setScaleModifier(sizeModifier);

                    CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(targetPlayer, new SizeChangedPacket(() -> targetPokemon, (double)sizeModifier));
                }
                context.getSource().sendSuccess(() -> Component.literal("The size of your team has been randomized"), true);
                return 0;
            }
            else {
                context.getSource().sendFailure((Component.literal("You don't have any Pokemon!")));
                return  -1;
            }
        }
        return -1;
    }

    private static int runResizer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

            String partyMember = StringArgumentType.getString(context, "member");
            double sizeModifier = DoubleArgumentType.getDouble(context, "size");

            return resize(context, targetPlayer, partyMember, (float)sizeModifier, false);
        }
        return -1;
    }

    private static int runRandomResizer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

            String partyMember = StringArgumentType.getString(context, "member");

            return resize(context, targetPlayer, partyMember, CobblemonSizeVariation.SIZER.getSize(), false);
        }
        return -1;
    }

    private static int runRandomSelfResizer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if(context.getSource().isPlayer()){
            ServerPlayer targetPlayer = context.getSource().getPlayer();

            if(targetPlayer == null) {
                context.getSource().sendFailure((Component.literal("You must be a player to run this command")));
                return  -1;
            }

            String partyMember = StringArgumentType.getString(context, "member");

            return resize(context, targetPlayer, partyMember, CobblemonSizeVariation.SIZER.getSize(), true);
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

            String partyMember = StringArgumentType.getString(context, "member");
            double sizeModifier = DoubleArgumentType.getDouble(context, "size");

            return resize(context, targetPlayer, partyMember, (float)sizeModifier, true);
        }
        return -1;
    }

    private static int resize(CommandContext<CommandSourceStack> context, ServerPlayer player, String pokemon, float size, boolean isSelf){
        PlayerPartyStore party = PlayerExtensionsKt.party(player);

        String success;
        String failure;

        Pokemon targetPokemon = null;
        if(getPartySlots().contains(pokemon)){
            int slot = PartySlot.valueOf(pokemon).getSlot();
            targetPokemon  = party.get(slot);

            success = isSelf ? String.format("The size of your pokemon in %s was changed!", PartySlot.valueOf(pokemon).getDisplayText()) :
                    String.format("The size of %s's pokemon in %s was changed!", player.getName().getString(), PartySlot.valueOf(pokemon).getDisplayText());

            failure = isSelf ? String.format("You have a have a pokemon in %s", PartySlot.valueOf(pokemon).getDisplayText()) :
                    String.format("%s doesn't have a pokemon in %s", player.getName().getString(), PartySlot.valueOf(pokemon).getDisplayText());
        }
        else{
            //targetPokemon = party.toGappyList().stream().filter(p -> p.getSpecies().getName().equals(pokemon)).findFirst().orElse(null);
            for(Pokemon pok : party.toGappyList()){
                if(pok == null) continue;
                if(pok.getSpecies().getName().equalsIgnoreCase(pokemon)) targetPokemon = pok;
            }
            if(targetPokemon != null){
                success = isSelf ? String.format("The size of your %s was changed!", targetPokemon.getSpecies().getName()) :
                        String.format("The size of %s's %s was changed!", player.getName().getString(), targetPokemon.getSpecies().getName());

                failure = "";
            } else {
                success = "";
                failure = isSelf ? String.format("You have a have a %s", pokemon) :
                        String.format("%s doesn't have a %s", player.getName().getString(), pokemon);
            }
        }

        if(targetPokemon != null){
            if(getPartySlots().contains(pokemon)){
                targetPokemon.setScaleModifier(size);
                context.getSource().sendSuccess(() -> Component.literal(success), true);
                Pokemon finalTargetPokemon = targetPokemon;
                CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(player, new SizeChangedPacket(() -> finalTargetPokemon, (double)size));
                return 0;
            }
            else {
                targetPokemon.setScaleModifier(size);
                context.getSource().sendSuccess(() -> Component.literal(success), true);
                Pokemon finalTargetPokemon1 = targetPokemon;
                CobblemonSizeVariation.platform.getNetworkManager().sendPacketToPlayer(player, new SizeChangedPacket(() -> finalTargetPokemon1, (double)size));
                return 0;
            }
        }
        else {
            context.getSource().sendFailure((Component.literal(failure)));
            return  -1;
        }
    }

    private static Set<String> getPartyMemberNames(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
        PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

        Set<String> partyMembers = new HashSet<>();

        party.forEach(p -> partyMembers.add(p.getSpecies().getName()));
        partyMembers.addAll(getPartySlots());
        return partyMembers;
    }

    private static Set<String> getSelfPartyMemberNames(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = context.getSource().getPlayer();
        if(targetPlayer != null){
            PlayerPartyStore party = PlayerExtensionsKt.party(targetPlayer);

            Set<String> partyMembers = new HashSet<>();

            party.forEach(p -> partyMembers.add(p.getSpecies().getName()));
            partyMembers.addAll(getPartySlots());
            return partyMembers;
        }
        return new HashSet<>();
    }

    private static Set<String> getPartySlots() {
        Set<String> slots = new HashSet<>();
        for(PartySlot partySlot : PartySlot.values()){
            slots.add(partySlot.toString());
        }
        return slots;
    }
}

