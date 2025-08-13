package net.ohacd.poh.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class PlayerStoryDataImpl implements PlayerStoryData, AutoSyncedComponent {
    private final Set<String> triggeredGlobal = new HashSet<>();
    private final Map<String, List<BlockPos>> triggeredLocations = new HashMap<>();

    @Override
    public boolean hasTriggered(String id) {
        return triggeredGlobal.contains(id);
    }

    @Override
    public boolean hasTriggeredAt(String id, BlockPos location, double minDistanceSq) {
        List<BlockPos> locations = triggeredLocations.get(id);
        if (locations != null) {
            for (BlockPos pos : locations) {
                if (pos.getSquaredDistance(location) <= minDistanceSq) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void trigger(String id, BlockPos location, boolean perLocation) {
        if (perLocation) {
            triggeredLocations.computeIfAbsent(id, k -> new ArrayList<>()).add(location);
        } else {
            triggeredGlobal.add(id);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {

    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        
    }
}
