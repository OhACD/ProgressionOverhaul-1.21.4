package net.ohacd.poh.world.poi.detector;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.ohacd.poh.world.poi.BlockPOI;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.POIMetadata;

import java.util.Set;

public final class BlockDetector implements Detector {
    private final String modid;
    private final Set<Block> targets;

    public BlockDetector(String modid, Set<Block> targets) {
        this.modid = modid;
        this.targets = targets;
    }

    private Identifier idFor(ServerWorld world, Block block, BlockPos pos) {
        var dim = world.getRegistryKey().getValue();
        var bId = Registries.BLOCK.getId(block);
        return Identifier.of(modid,
                "block/" + dim.getNamespace() + "/" + dim.getPath() + "/" +
                        bId.getNamespace() + "/" + bId.getPath() + "/" + pos.asLong());
    }

    @Override
    public void onChunkLoad(ServerWorld world, Chunk chunk, POIIndex index) {
        ChunkPos cp = chunk.getPos();
        int minY = world.getBottomY();
        int maxY = world.getTopYInclusive();
        BlockPos.Mutable m = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = minY; y < maxY; y++) {
                    m.set(cp.getStartX() + x, y, cp.getStartZ() + z);
                    var st = chunk.getBlockState(m);
                    var b = st.getBlock();
                    if (!targets.contains(b)) continue;

                    Identifier id = idFor(world, b, m);
                    POIMetadata meta = new POIMetadata();
                    index.add(new BlockPOI(id, world.getRegistryKey(), m.toImmutable(), meta, b));
                }
    }

    @Override
    public void onBlockPlaced(ServerWorld world, BlockPos pos, net.minecraft.block.BlockState state, POIIndex index) {
        var b = state.getBlock();
        if (!targets.contains(b)) return;
        Identifier id = idFor(world, b, pos);
        index.add(new BlockPOI(id, world.getRegistryKey(), pos.toImmutable(), new POIMetadata(), b));
    }

    @Override
    public void onBlockBroken(ServerWorld world, BlockPos pos, net.minecraft.block.BlockState state, POIIndex index) {
        var b = state.getBlock();
        if (!targets.contains(b)) return;
        Identifier id = idFor(world, b, pos);
        index.remove(id);
    }
}