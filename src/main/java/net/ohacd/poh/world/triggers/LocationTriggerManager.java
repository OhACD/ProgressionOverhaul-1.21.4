package net.ohacd.poh.world.triggers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventDispatcher;
import net.ohacd.poh.world.events.TriggerEventType;

import java.util.ArrayList;
import java.util.List;

public class LocationTriggerManager implements ServerTickEvents.EndTick{
    private final List<Trigger> triggers = new ArrayList<>();
    private final PlayerTriggerTracker movement = new PlayerTriggerTracker(4);
    private long lastTick = 0L;

    public void register(Trigger trigger) {triggers.add(trigger); }

    @Override
    public void onEndTick(MinecraftServer minecraftServer) {
        long trigger = minecraftServer.getOverworld().getTime();
        if ((trigger - lastTick) < 20) return;
        lastTick = trigger;

        var players = minecraftServer.getPlayerManager().getPlayerList();
        for (var player : players) {
            if (!movement.movedEnough(player)) continue;

            for (var trig : triggers) {
                var result = trig.check(player);
                if (result.fired()) {
                    TriggerEventType type = switch (trig.type()) {
                        case STRUCTURE -> TriggerEventType.ENTER_STRUCTURE;
                        case BLOCK_PROXIMITY -> TriggerEventType.NEAR_BLOCK;
                        case ZONE -> TriggerEventType.ENTER_ZONE;
                    };
                    TriggerEventDispatcher.post(new TriggerEvent(
                            type,
                            result.triggerId(),
                            result.subjectId(),
                            player,
                            result.subjectPos()));
                    break;
                }
            }
        }
    }
}
