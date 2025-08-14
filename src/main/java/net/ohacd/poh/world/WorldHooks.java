package net.ohacd.poh.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.detector.Detector;

import java.util.ArrayList;
import java.util.List;

public final class WorldHooks {
    private static final List<Detector> DETECTORS = new ArrayList<>();

    private WorldHooks() {}

    public static void registerDetector(Detector detector) { DETECTORS.add(detector); }

    public static void init() {
        // Scan structures/blocks when chunks load
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            if (world.isClient) return;
            POIIndex index = POIIndex.get((ServerWorld) world);
            for (var d : DETECTORS) {
                d.onChunkLoad((ServerWorld) world, chunk, index);
            }
        });

        // Simulate block place event
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world.isClient) return ActionResult.PASS;

            // Schedule for next tick to ensure block is actually placed
            BlockPos placedPos = hit.getBlockPos().offset(hit.getSide());
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                if (world.getBlockState(placedPos).isAir()) return;
                POIIndex index = POIIndex.get((ServerWorld) world);
                for (var d : DETECTORS) {
                    d.onBlockPlaced((ServerWorld) world, placedPos, world.getBlockState(placedPos), index);
                }
            });

            return ActionResult.PASS;
        });

        // Track block breaking
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return;
            POIIndex index = POIIndex.get((ServerWorld) world);
            for (var d : DETECTORS) {
                d.onBlockBroken((ServerWorld) world, pos, state, index);
            }
        });
    }
}
