package net.ohacd.poh.world.events;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public record TriggerEvent(TriggerEventType type,
                           Identifier triggerId,
                           Identifier subjectId,
                           ServerPlayerEntity player,
                           @Nullable BlockPos pos) {}
