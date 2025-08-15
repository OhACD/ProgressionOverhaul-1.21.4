package net.ohacd.poh.world.triggers;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface Trigger {
    TriggerType type();
    Identifier id();
    TriggerResult check(ServerPlayerEntity player);

    // Default is true
    default boolean requiresMovement() {
        return true;
    }

    // Builder-style method
    default Trigger withMovement(boolean requiresMovement) {
        Trigger self = this;
        return new Trigger() {
            @Override
            public TriggerType type() {
                return self.type();
            }

            @Override
            public Identifier id() {
                return self.id();
            }

            @Override
            public TriggerResult check(ServerPlayerEntity player) {
                return self.check(player);
            }

            @Override
            public boolean requiresMovement() {
                return requiresMovement;
            }
        };
    }
}