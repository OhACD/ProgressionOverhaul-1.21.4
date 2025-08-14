package net.ohacd.poh.world.poi.detector;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.POIMetadata;
import net.ohacd.poh.world.poi.StructurePOI;

import java.util.Set;

public class StructureDetector implements Detector {
    private final Set<RegistryKey<Structure>> targets;
    private final String modid;

    public StructureDetector(String modid, Set<RegistryKey<net.minecraft.world.gen.structure.Structure>> targets) {
        this.modid = modid;
        this.targets = targets;
    }

    private Identifier idFor(ServerWorld world, RegistryKey<net.minecraft.world.gen.structure.Structure> key, BlockPos pos) {
        var dim = world.getRegistryKey().getValue();
        return Identifier.of(modid,
                "structure/" + dim.getNamespace() + "/" + dim.getPath() + "/" +
                        key.getValue().getNamespace() + "/" + key.getValue().getPath() + "/" +
                        pos.asLong());
    }

    @Override
    public void onChunkLoad(ServerWorld world, Chunk chunk, POIIndex index) {
        var structureStartMap = world.getStructureAccessor().getStructureStarts(chunk.getPos());
        for (var entry : structureStartMap.entrySet()) {
            var structureKey = entry.getKey();
            if (!targets.contains(structureKey)) continue;

            var start = entry.getValue();
            if (start == null || !start.hasChildren()) continue;

            BlockPos center = start.getBoundingBox().getCenter();
            Identifier id = idFor(world, structureKey, center);

            POIMetadata meta = new POIMetadata();
            meta.putString("name", structureKey.getValue().toString());
            meta.putBool("from_scan", true);

            index.add(new StructurePOI(id, world.getRegistryKey(), center, meta, structureKey));
        }
    }

    @Override
    public void onBlockPlaced(ServerWorld world, BlockPos pos, net.minecraft.block.BlockState state, POIIndex index) {
        // Structures don’t get registered on block placement
    }

    @Override
    public void onBlockBroken(ServerWorld world, BlockPos pos, net.minecraft.block.BlockState state, POIIndex index) {
        // Structure POIs should persist unless the whole structure is removed
    }
}
