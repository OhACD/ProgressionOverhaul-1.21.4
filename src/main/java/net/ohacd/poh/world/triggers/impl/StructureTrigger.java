package net.ohacd.poh.world.triggers.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.world.triggers.Trigger;
import net.ohacd.poh.world.triggers.TriggerResult;
import net.ohacd.poh.world.triggers.TriggerType;

public class StructureTrigger implements Trigger {
    private final Identifier cfgId;
    private final Identifier structureId;
    private final double radius;

    public StructureTrigger(Identifier cfgId, Identifier structureId, double radius) {
        this.cfgId = cfgId;
        this.structureId = structureId;
        this.radius = radius;
    }

    @Override
    public TriggerType type() {
        return TriggerType.STRUCTURE;
    }

    @Override
    public Identifier id() {
        return cfgId;
    }

    @Override
    public TriggerResult check(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();
        //
         //
        boolean near = isNearStructure(world, pos, structureId, radius);
        if (near) return TriggerResult.hit(cfgId, structureId, pos);
        return TriggerResult.miss();
    }

    private boolean isNearStructure(ServerWorld world, BlockPos pos, Identifier structureId, double radius) {
        //
         //
         //
        return false;
    }
}
