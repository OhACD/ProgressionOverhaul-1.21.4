package net.ohacd.poh.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;

public class ModTickHandlers {
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!world.isClient) {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
                    fatigue.tick(player);
                }
            }
        });
    }

}
