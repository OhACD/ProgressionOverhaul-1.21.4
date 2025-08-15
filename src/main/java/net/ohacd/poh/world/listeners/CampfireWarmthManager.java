package net.ohacd.poh.world.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventListener;
import net.ohacd.poh.world.events.TriggerEventType;
import net.ohacd.poh.world.poi.BlockPOI;
import net.ohacd.poh.world.poi.POI;
import net.ohacd.poh.world.poi.POIIndex;

import java.util.*;

public class CampfireWarmthManager implements TriggerEventListener {
    private static final float FATIGUE_DECREASE_PER_TICK = 0.0009F;
    private static final Set<UUID> playersNearCampfire = new HashSet<>();
    private static final int CHECK_INTERVAL_TICKS = 20;

    @Override
    public void onTrigger(TriggerEvent event) {
        if (event.type() != TriggerEventType.NEAR_BLOCK) return;
        if (!event.subjectId().toString().contains("campfire")) return;

        ServerPlayerEntity player = event.player();
        UUID playerId = player.getUuid();

        // Entry message sent only once
        if (!playersNearCampfire.contains(playerId)) {
            playersNearCampfire.add(playerId);
            player.sendMessage(
                    Text.literal("You are near a Campfire, you are starting to feel rested")
                            .formatted(Formatting.GREEN), true);
        }

        // Fatigue reduction
        FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
        fatigue.decreaseFatigue(FATIGUE_DECREASE_PER_TICK);
    }

    // Checks exits
    public static void checkExits(ServerWorld world) {
        List<UUID> currentNearCampfire = new ArrayList<>();

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (isNearCampfire(player)) {
                currentNearCampfire.add(player.getUuid());
            }
        }

        // Detects Exits
        for (UUID uuid : playersNearCampfire) {
            if (!currentNearCampfire.contains(uuid)) {
                ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);
                if (player != null) {
                    player.sendMessage(
                            Text.literal("You are no longer near a Campfire.")
                                    .formatted(Formatting.RED), true);
                }
            }
        }

        // Update tracking
        playersNearCampfire.clear();
        playersNearCampfire.addAll(currentNearCampfire);
    }

    // Helper to check if player is near any campfire
    private static boolean isNearCampfire(ServerPlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        ServerWorld world = player.getServerWorld();
        POIIndex index = POIIndex.get(world);
        ChunkPos chunk = new ChunkPos(pos);
        List<POI> nearby = index.nearby(world, chunk, 1);

        for (POI poi : nearby) {
            if (poi instanceof BlockPOI blockPOI &&
                    (blockPOI.block() == Blocks.CAMPFIRE || blockPOI.block() == Blocks.SOUL_CAMPFIRE)) {
                double distSq = player.getPos().squaredDistanceTo(
                        blockPOI.pos().getX() + 0.5,
                        blockPOI.pos().getY() + 0.5,
                        blockPOI.pos().getZ() + 0.5
                );
                if (distSq <= 12 * 12) {
                    return true;
                }
            }
        }
        return false;
    }

    // Register periodic check
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % CHECK_INTERVAL_TICKS == 0) {
                for (ServerWorld world : server.getWorlds()) {
                    checkExits(world);
                }
            }
        });
    }
}
