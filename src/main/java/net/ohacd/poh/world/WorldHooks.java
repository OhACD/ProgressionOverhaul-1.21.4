package net.ohacd.poh.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.detector.Detector;

import java.util.ArrayList;
import java.util.List;

public class WorldHooks {
    private static final List<Detector> DETECTORS = new ArrayList<>();

    public static void registerDetector(Detector detector) {
        DETECTORS.add(detector);
    }

    public static void init() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            POIIndex index = getPOIIndex(world);
            for (Detector detector : DETECTORS) {
                detector.onChunkLoad(world, chunk, index);
            }
        });

        PlayerBlockPlaceEvents.AFTER.register((world, player, pos, state, stack) -> {
            if (world.isClient) return;
            POIIndex index = getPOIIndex((ServerWorld) world);
            for (Detector detector : DETECTORS) {
                detector.onBlockPlaced((ServerWorld) world, pos, state, index);
            }
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return;
            POIIndex index = getPOIIndex((ServerWorld) world);
            for (Detector detector : DETECTORS) {
                detector.onBlockBroken((ServerWorld) world, pos, state, index);
            }
        });

        ServerTickEvents.END_WORLD_TICK.register(WorldHooks::onWorldTick);
    }

    private static POIIndex getPOIIndex(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                (nbt, regs) -> POIIndex.fromNbt(nbt, regs),
                POIIndex::new,
                "poi_index"
        );
    }

    private static void onWorldTick(ServerWorld world) {
        // This is where you could run trigger checks for players
        for (ServerPlayerEntity player : world.getPlayers()) {
            // Example: trigger scanning
        }
    }
}
