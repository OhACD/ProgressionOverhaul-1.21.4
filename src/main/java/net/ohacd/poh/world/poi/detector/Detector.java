package net.ohacd.poh.world.poi.detector;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.ohacd.poh.world.poi.POIIndex;

public interface Detector {
    void onChunkLoad(ServerWorld world, Chunk chunk, POIIndex index);
    void onBlockPlaced(ServerWorld world, BlockPos pos, BlockState state, POIIndex index);
    void onBlockBroken(ServerWorld world, BlockPos pos, BlockState state, POIIndex index);
}
