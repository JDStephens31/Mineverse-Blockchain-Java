package com.jstephens.mineverse.command;


import com.jstephens.mineverse.http.OkHttp3;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;


public class MineCommand {
    public MineCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("create").then(Commands.literal("wallet").executes((command) -> {
            try {
                return setHome(command.getSource());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })));

    }

    private int setHome(CommandSourceStack source) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        String name = player.getStringUUID();
        source.sendSuccess(new TextComponent("Player is " + name), true);
        OkHttp3.sendPost(name);
        return 1;
    }
}

