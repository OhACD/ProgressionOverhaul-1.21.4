package net.ohacd.poh.world.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Blocks;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.world.poi.BlockPOI;
import net.ohacd.poh.world.poi.POI;
import net.ohacd.poh.world.poi.POIIndex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CampfireFatigueManager {

    private static final float FATIGUE_DECREASE_PER_TICK = 0.0009F;
    private static final int CAMPFIRE_RADIUS = 12; // blocks

    // Track players currently near a campfire
    private static final Set<UUID> playersNearCampfire = new HashSet<>();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(CampfireFatigueManager::tickWorld);
    }

    private static void tickWorld(ServerWorld world) {
        POIIndex poiIndex = POIIndex.get(world);

        Set<UUID> tickNearCampfire = new HashSet<>();

        for (ServerPlayerEntity player : world.getPlayers()) {
            ChunkPos playerChunk = new ChunkPos(player.getBlockPos());
            List<POI> nearbyPOIs = poiIndex.nearby(world, playerChunk, 1);

            boolean nearCampfire = false;
            Vec3d playerPos = player.getPos();

            for (POI poi : nearbyPOIs) {
                if (!(poi instanceof BlockPOI blockPOI)) continue;
                if (blockPOI.block() != Blocks.CAMPFIRE && blockPOI.block() != Blocks.SOUL_CAMPFIRE) continue;

                double distSq = playerPos.squaredDistanceTo(
                        blockPOI.pos().getX() + 0.5,
                        blockPOI.pos().getY() + 0.5,
                        blockPOI.pos().getZ() + 0.5
                );

                if (distSq <= CAMPFIRE_RADIUS * CAMPFIRE_RADIUS) {
                    nearCampfire = true;
                    break; // stop at first nearby campfire
                }
            }

            UUID playerId = player.getUuid();

            // Handle entry message
            if (nearCampfire && !playersNearCampfire.contains(playerId)) {
                player.sendMessage(Text.literal("You are near a Campfire, you are starting to feel rested")
                        .formatted(Formatting.GREEN), true);
            }

            // Handle exit message
            if (!nearCampfire && playersNearCampfire.contains(playerId)) {
                player.sendMessage(Text.literal("You are no longer near a Campfire.")
                        .formatted(Formatting.RED), true);
            }

            if (nearCampfire) {
                // Apply fatigue decrease
                FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
                fatigue.decreaseFatigue(FATIGUE_DECREASE_PER_TICK);
                tickNearCampfire.add(playerId);
            }
        }

        // Update global set for next tick
        playersNearCampfire.clear();
        playersNearCampfire.addAll(tickNearCampfire);
    }
}
