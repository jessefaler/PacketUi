package com.protoxon.promenu;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.protoxon.promenu.menus.SelectionMenu;
import com.protoxon.promenu.service.Menu;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import static com.protoxon.promenu.ProMenu.proxy;

public class MenuCommand {
    public static void register() {
        CommandManager commandManager = proxy.getCommandManager();
        // Create the root command
        LiteralArgumentBuilder<CommandSource> root = LiteralArgumentBuilder.literal("menu");

        root.executes(MenuCommand::handleRootCommand); // Handle the execution of /sls when no arguments are given

        // Create the Brigadier command
        BrigadierCommand brigadierCommand = new BrigadierCommand(root);

        // Create command metadata
        CommandMeta commandMeta = commandManager.metaBuilder("menu")
                .plugin(ProMenu.plugin)
                .build();

        // Register the command
        commandManager.register(commandMeta, brigadierCommand);
    }

    private static int handleRootCommand(CommandContext<CommandSource> context) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(context.getSource());
        Player player = (Player) context.getSource();
        Menu menu = new SelectionMenu(user);
        return 0;
    }
}
