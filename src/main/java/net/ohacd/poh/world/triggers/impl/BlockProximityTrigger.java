package net.ohacd.poh.world.triggers.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.world.triggers.Trigger;
import net.ohacd.poh.world.triggers.TriggerResult;
import net.ohacd.poh.world.triggers.TriggerType;

import java.util.Set;
import java.util.function.Predicate;

public class BlockProximityTrigger implements Trigger {
    private final Identifier cfgId;
    private final Set<Block> blocks;
    private final int radius;
    private final Predicate<BlockState> stateFilter;

    public BlockProximityTrigger(Identifier cfgId, Set<Block> blocks, int radius) {
        this(cfgId, blocks, radius, state -> true);
    }

    public BlockProximityTrigger(Identifier cfgId, Set<Block> blocks, int radius, Predicate<BlockState> stateFilter) {
        this.cfgId = cfgId;
        this.blocks = blocks;
        this.radius = radius;
        this.stateFilter = stateFilter;
    }

    @Override public TriggerType type() {
        return TriggerType.BLOCK_PROXIMITY;
    }
    @Override public Identifier id() {
        return cfgId;
    }

    @Override
    public TriggerResult check(ServerPlayerEntity player) {
        BlockPos center = player.getBlockPos();
        ServerWorld world = player.getServerWorld();
        BlockPos.Mutable m = new BlockPos.Mutable();

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++) {
                    m.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    var state = world.getBlockState(m);
                    var b = state.getBlock();
                    if (blocks.contains(b) && stateFilter.test(state)) {
                        Identifier blockId = Registries.BLOCK.getId(b);
                        return TriggerResult.hit(cfgId, blockId, m.toImmutable());
                    }
                }
        return TriggerResult.miss();
    }
}
