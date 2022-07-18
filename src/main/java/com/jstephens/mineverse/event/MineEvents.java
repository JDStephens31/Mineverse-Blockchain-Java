package com.jstephens.mineverse.event;

import com.jstephens.mineverse.Mineverse;
import com.jstephens.mineverse.command.MineCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Mineverse.MOD_ID)
public class MineEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MineCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if(!event.getOriginal().getLevel().isClientSide()) {
            event.getPlayer().getPersistentData().putIntArray(Mineverse.MOD_ID + "homepos",
                    event.getOriginal().getPersistentData().getIntArray(Mineverse.MOD_ID + "homepos"));
        }
    }
}