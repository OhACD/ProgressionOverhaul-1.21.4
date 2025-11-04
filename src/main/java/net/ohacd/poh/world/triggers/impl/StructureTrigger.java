package net.ohacd.poh.world.triggers.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.ohacd.poh.world.poi.POI;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.StructurePOI;
import net.ohacd.poh.world.triggers.Trigger;
import net.ohacd.poh.world.triggers.TriggerResult;
import net.ohacd.poh.world.triggers.TriggerType;

import java.util.List;

public class StructureTrigger implements Trigger {
    private final Identifier cfgId;
    private final Identifier structureId;
    private final double radius;

    public StructureTrigger(Identifier cfgId, Identifier structureId, double radius) {
        this.cfgId = cfgId;
        this.structureId = structureId;
        this.radius = radius;
    }

    @Override public TriggerType type() { return TriggerType.STRUCTURE; }
    @Override public Identifier id() { return cfgId; }

    @Override
    public TriggerResult check(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();

        POIIndex index = POIIndex.get(world);
        int rChunks = Math.max(1, (int) Math.ceil(radius / 16.0));
        List<POI> nearby = index.nearby(world, new ChunkPos(pos), rChunks);

        for (POI poi : nearby) {
            if (!(poi instanceof StructurePOI s)) continue;
            if (!s.structureId().equals(structureId)) continue;

            // If inside structure bounds OR within radius of center → fire
            if (s.bounds().contains(Vec3d.ofCenter(pos)) ||
                    pos.getSquaredDistance(s.center()) <= (radius * radius)) {
                return TriggerResult.hit(cfgId, structureId, s.center());
            }
        }
        return TriggerResult.miss();
    }
}