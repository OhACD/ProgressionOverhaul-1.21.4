package net.ohacd.poh.world.poi.detector;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.POIMetadata;
import net.ohacd.poh.world.poi.StructurePOI;

import java.util.Map;
import java.util.Set;

/**
 * Reads structure starts from the loaded chunk and indexes only the selected structures.
 * This relies on the structure data present in the chunk; it does not "locate" faraway structures, yet...
 */
public final class StructureDetector implements Detector {
    private final String modid;
    private final Set<Identifier> targetStructureIds;

    public StructureDetector(String modid, Set<Identifier> targetStructureIds) {
        this.modid = modid;
        this.targetStructureIds = targetStructureIds;
    }

    public static StructureDetector vanillaTargets(String modid, Set<Identifier> ids) {
        return new StructureDetector(modid, ids);
    }

    private Identifier idFor(ServerWorld world, Identifier structureId, BlockPos center) {
        var dim = world.getRegistryKey().getValue();
        return Identifier.of(
                modid,
                "structure/" + dim.getNamespace() + "/" + dim.getPath() + "/" +
                        structureId.getNamespace() + "/" + structureId.getPath() + "/" +
                        center.asLong()
        );
    }

    @Override
    public void onChunkLoad(ServerWorld world, Chunk chunk, POIIndex index) {
        Map<Structure, StructureStart> starts = chunk.getStructureStarts();
        if (starts == null || starts.isEmpty()) return;

        // Uses the world structure registry once, then reuses.
        Registry<Structure> structureRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);

        for (var e : starts.entrySet()) {
            Structure structure = e.getKey();


            Identifier structureId = structureRegistry.getId(structure);
            if (structureId == null) continue; // skips unregistered structures

            if (!targetStructureIds.contains(structureId)) continue;

            StructureStart start = e.getValue();
            if (start == null || !start.hasChildren()) continue;

            BlockBox blockBox = start.getBoundingBox();
            Box bb = new Box(
                    blockBox.getMinX(),
                    blockBox.getMinY(),
                    blockBox.getMinZ(),
                    blockBox.getMaxX() + 1.0,
                    blockBox.getMaxY() + 1.0,
                    blockBox.getMaxZ() + 1.0
            );

            BlockPos center = BlockPos.ofFloored(bb.getCenter());
            Identifier id = idFor(world, structureId, center);

            index.add(new StructurePOI(
                    id,
                    world.getRegistryKey(),
                    bb,
                    center,
                    new POIMetadata(),
                    structureId
            ));
        }
    }

    @Override
    public void onBlockPlaced(ServerWorld world, BlockPos pos, BlockState state, POIIndex index) {}

    @Override
    public void onBlockBroken(ServerWorld world, BlockPos pos, BlockState state, POIIndex index) {}
}
