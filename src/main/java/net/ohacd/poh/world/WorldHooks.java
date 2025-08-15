package net.ohacd.poh.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ohacd.poh.world.poi.POIIndex;
import net.ohacd.poh.world.poi.detector.Detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class WorldHooks {
    private static final List<Detector> DETECTORS = new ArrayList<>();

    private WorldHooks() {}

    public static void registerDetector(Detector detector) {
        DETECTORS.add(detector);
    }

    public static void init() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            if (world.isClient) return;
            POIIndex index = POIIndex.get(world);
            for (var d : DETECTORS) {
                d.onChunkLoad(world, chunk, index);
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world.isClient) return ActionResult.PASS;

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

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return;
            POIIndex index = POIIndex.get((ServerWorld) world);
            for (var d : DETECTORS) {
                d.onBlockBroken((ServerWorld) world, pos, state, index);
            }
        });

        // Add async task for heavy operations
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Optional<ServerWorld> optionalWorld = Optional.ofNullable(server.getWorld(World.OVERWORLD));
            if (optionalWorld.isPresent()) {
                ServerWorld world = optionalWorld.get();
                server.execute(() -> {
                    // Heavy operations here (e.g., POI indexing)
                });
            } else {
                // Handle missing world (e.g., log a warning)
                System.out.println("Overworld not found!");
            }
        });
    }
}
