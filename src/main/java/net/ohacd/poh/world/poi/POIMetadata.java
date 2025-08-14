package net.ohacd.poh.world.poi;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public final class POIMetadata {
    private final Map<String, Integer> ints = new HashMap<>();
    private final Map<String, Boolean> bools = new HashMap<>();

    public void putInt(String k, int v) { ints.put(k, v); }
    public int getInt(String k, int d) { return ints.getOrDefault(k, d); }
    public void putBool(String k, boolean v) { bools.put(k, v); }
    public boolean getBool(String k, boolean d) { return bools.getOrDefault(k, d); }

    public NbtCompound toNbt() {
        NbtCompound out = new NbtCompound();
        NbtList intsList = new NbtList();
        for (var e : ints.entrySet()) {
            NbtCompound c = new NbtCompound();
            c.putString("k", e.getKey());
            c.putInt("v", e.getValue());
            intsList.add(c);
        }
        out.put("ints", intsList);
        NbtList boolsList = new NbtList();
        for (var e : bools.entrySet()) {
            NbtCompound c = new NbtCompound();
            c.putString("k", e.getKey());
            c.putBoolean("v", e.getValue());
            boolsList.add(c);
        }
        out.put("bools", boolsList);
        return out;
    }

    public static POIMetadata fromNbt(NbtCompound nbt) {
        POIMetadata m = new POIMetadata();
        for (NbtElement el : nbt.getList("ints", NbtElement.COMPOUND_TYPE)) {
            NbtCompound c = (NbtCompound) el;
            m.putInt(c.getString("k"), c.getInt("v"));
        }
        for (NbtElement el : nbt.getList("bools", NbtElement.COMPOUND_TYPE)) {
            NbtCompound c = (NbtCompound) el;
            m.putBool(c.getString("k"), c.getBoolean("v"));
        }
        return m;
    }
}