package net.ohacd.poh.world.poi;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class POIIndex extends PersistentState {
    private final Long2ObjectMap<List<Identifier>> byChunk = new Long2ObjectOpenHashMap<>();
    private final Map<Identifier, POI> all = new HashMap<>();

    public void add(POI poi) {
        all.put(poi.id(), poi);
        long ck = ChunkPos.toLong(poi.center());
        byChunk.computeIfAbsent(ck, k -> new ArrayList<>()).add(poi.id());
        markDirty();
    }

    public void remove(Identifier id) {
        POI poi = all.remove(id); if (poi == null) return;
        long ck = ChunkPos.toLong(poi.center());
        var list = byChunk.get(ck);
        if (list != null) { list.remove(id); if (list.isEmpty()) byChunk.remove(ck); }
        markDirty();
    }

    public List<POI> nearby(ServerWorld world, ChunkPos center, int rChunks) {
        var found = new ArrayList<POI>();
        for (int dx = -rChunks; dx <= rChunks; dx++) for (int dz = -rChunks; dz <= rChunks; dz++) {
            long ck = ChunkPos.toLong(center.x + dx, center.z + dz);
            var ids = byChunk.get(ck); if (ids == null) continue;
            for (var id : ids) {
                var poi = all.get(id);
                if (poi != null && poi.dimension().equals(world.getRegistryKey())) found.add(poi); // ✅ equals
            }
        }
        return found;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();
        for (POI poi : all.values()) {
            NbtCompound c = new NbtCompound();
            c.putString("id", poi.id().toString());
            c.putString("dimension", poi.dimension().getValue().toString());
            c.put("meta", poi.meta().toNbt());
            if (poi instanceof BlockPOI b) {
                c.putString("kind", "block");
                c.putInt("x", b.pos().getX());
                c.putInt("y", b.pos().getY());
                c.putInt("z", b.pos().getZ());
                c.putString("block", Registries.BLOCK.getId(b.block()).toString());
            } else if (poi instanceof StructurePOI s) {
                c.putString("kind", "structure");
                c.put("bounds", boxToNbt(s.bounds()));
                c.putInt("cx", s.center().getX());
                c.putInt("cy", s.center().getY());
                c.putInt("cz", s.center().getZ());
                c.putString("structure", s.structureId().toString());
            }
            list.add(c);
        }
        nbt.put("all", list);
        return nbt;
    }

    public static POIIndex fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        POIIndex index = new POIIndex();
        for (NbtElement el : nbt.getList("all", NbtElement.COMPOUND_TYPE)) {
            NbtCompound c = (NbtCompound) el;
            Identifier id = Identifier.of(c.getString("id"));
            RegistryKey<World> dim = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(c.getString("dimension")));
            POIMetadata meta = POIMetadata.fromNbt(c.getCompound("meta"));
            String kind = c.getString("kind");
            if ("block".equals(kind)) {
                BlockPos pos = new BlockPos(c.getInt("x"), c.getInt("y"), c.getInt("z"));
                var blockId = Identifier.of(c.getString("block"));
                Block block = Registries.BLOCK.get(blockId);
                index.add(new BlockPOI(id, dim, pos, meta, block));
            } else if ("structure".equals(kind)) {
                Box bounds = boxFromNbt(c.getCompound("bounds"));
                BlockPos center = new BlockPos(c.getInt("cx"), c.getInt("cy"), c.getInt("cz"));
                Identifier sid = Identifier.of(c.getString("structure"));
                index.add(new StructurePOI(id, dim, bounds, center, meta, sid));
            }
        }
        index.markDirty();
        return index;
    }

    public static NbtCompound boxToNbt(Box b) {
        NbtCompound c = new NbtCompound();
        c.putDouble("minX", b.minX); c.putDouble("minY", b.minY); c.putDouble("minZ", b.minZ);
        c.putDouble("maxX", b.maxX); c.putDouble("maxY", b.maxY); c.putDouble("maxZ", b.maxZ);
        return c;
    }
    public static Box boxFromNbt(NbtCompound c) {
        return new Box(c.getDouble("minX"), c.getDouble("minY"), c.getDouble("minZ"),
                c.getDouble("maxX"), c.getDouble("maxY"), c.getDouble("maxZ"));
    }

    public static POIIndex get(ServerWorld world) {
        var storage = world.getPersistentStateManager();
        // ✅ read from disk when present
        return world.getPersistentStateManager().getOrCreate(
                (nbt, regs) -> POIIndex.fromNbt(nbt, regs),
                POIIndex::new,
                        (nbt, regs) -> POIIndex.fromNbt(nbt, regs),
                        "poh_poi_index").create(storage), // see alternative below if this overload is unavailable
                "poh_poi_index");
    }
}
