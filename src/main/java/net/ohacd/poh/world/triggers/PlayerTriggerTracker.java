package net.ohacd.poh.world.triggers;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerTriggerTracker {
    private final Map<UUID, BlockPos> lastCheckPos = new HashMap<>();
    private final int minMoveSq;

    public PlayerTriggerTracker(int minMove) { this.minMoveSq = minMove * minMove; }

    public boolean movedEnough(ServerPlayerEntity player) {
        BlockPos now = player.getBlockPos();
        BlockPos prev = lastCheckPos.get(player.getUuid());
        if (prev == null) { lastCheckPos.put(player.getUuid(), now); return true; }
        int dx = now.getX() - prev.getX();
        int dy = now.getY() - prev.getY();
        int dz = now.getZ() - prev.getZ();
        int dist2 = dx*dx + dy*dy + dz*dz;
        if (dist2 >= minMoveSq) { lastCheckPos.put(player.getUuid(), now); return true; }
        return false;
    }

    public void clear(ServerPlayerEntity player) { lastCheckPos.remove(player.getUuid()); }
}