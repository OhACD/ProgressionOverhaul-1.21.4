package net.ohacd.poh.world.triggers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventDispatcher;
import net.ohacd.poh.world.events.TriggerEventType;

import java.util.*;

public class LocationTriggerManager implements ServerTickEvents.EndTick {
    private final List<Trigger> triggers = new ArrayList<>();
    private final PlayerTriggerTracker movement = new PlayerTriggerTracker(4);
    private long lastTick = 0L;
    private final int TICK_INTERVAL = 20;   // checks roughly once a second

    public void register(Trigger trigger) { triggers.add(trigger); }

    @Override
    public void onEndTick(MinecraftServer server) {
        long t = server.getOverworld().getTime();
        if ((t - lastTick) < TICK_INTERVAL) return;
        lastTick = t;

        var players = server.getPlayerManager().getPlayerList();
        for (var player : players) {
            boolean moved = false; // we'll compute this only if needed

            for (var trig : triggers) {
                if (trig.requiresMovement()) {
                    // Only calculate once per player per tick
                    if (!moved && !movement.movedEnough(player)) {
                        continue; // skip this trigger if player hasn't moved
                    }
                    moved = true; // remember that we've checked movement
                }

                var result = trig.check(player);
                if (result.fired()) {
                    TriggerEventType type = switch (trig.type()) {
                        case STRUCTURE -> TriggerEventType.ENTER_STRUCTURE;
                        case BLOCK_PROXIMITY -> TriggerEventType.NEAR_BLOCK;
                        case ZONE -> TriggerEventType.ENTER_ZONE;
                    };
                    TriggerEventDispatcher.post(new TriggerEvent(
                            type, result.triggerId(), result.subjectId(), player, result.subjectPos()));
                    break; // fire at most one per scan cycle per player
                }
            }
        }
    }
}
