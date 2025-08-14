package net.ohacd.poh.world.triggers.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.ohacd.poh.world.triggers.Trigger;
import net.ohacd.poh.world.triggers.TriggerResult;
import net.ohacd.poh.world.triggers.TriggerType;
import net.ohacd.poh.world.triggers.zones.TriggerZone;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ZoneTrigger implements Trigger {
    private final TriggerZone zone;
    private final Set<UUID> inside = new HashSet<>();

    public ZoneTrigger(TriggerZone zone) {
        this.zone = zone; }


    @Override
    public TriggerType type() {
        return TriggerType.ZONE;
    }

    @Override
    public Identifier id() {
        return zone.id();
    }

    @Override
    public TriggerResult check(ServerPlayerEntity player) {
        boolean in = zone.contains(player);
        boolean was = inside.contains(player.getUuid());
        if (in && !was) {
            inside.add(player.getUuid());
            return TriggerResult.hit(id(), zone.id(), player.getBlockPos());
        } else if (!in && was) {
            inside.remove(player.getUuid());
        }
        return TriggerResult.miss();
    }
}
