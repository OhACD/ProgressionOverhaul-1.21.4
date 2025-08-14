package net.ohacd.poh.world.triggers.impl;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.world.triggers.Trigger;
import net.ohacd.poh.world.triggers.TriggerResult;
import net.ohacd.poh.world.triggers.TriggerType;

import java.util.Set;

public class BlockProximityTrigger implements Trigger {
    private final Identifier cfgId;
    private final Set<Block> blocks;
    private final int radius;

    public BlockProximityTrigger(Identifier cfgId, Set<Block> blocks, int radius) {
        this.cfgId = cfgId;
        this.blocks = blocks;
        this.radius = radius;
    }

    @Override public TriggerType type() { return TriggerType.BLOCK_PROXIMITY; }
    @Override public Identifier id() { return cfgId; }

    @Override
    public TriggerResult check(ServerPlayerEntity player) {
        BlockPos center = player.getBlockPos();
        ServerWorld world = player.getServerWorld();
        BlockPos.Mutable m = new BlockPos.Mutable();

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++) {
                    m.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    var b = world.getBlockState(m).getBlock();
                    if (blocks.contains(b)) {
                        Identifier blockId = Registries.BLOCK.getId(b);
                        return TriggerResult.hit(cfgId, blockId, m.toImmutable());
                    }
                }
        return TriggerResult.miss();
    }
}