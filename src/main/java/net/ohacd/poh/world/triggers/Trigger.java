package net.ohacd.poh.world.triggers;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface Trigger {
    TriggerType type();
    Identifier id();

    TriggerResult check(ServerPlayerEntity player);
}
