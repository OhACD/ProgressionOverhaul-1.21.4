package net.ohacd.poh.world.listeners;

import net.minecraft.server.network.ServerPlayerEntity;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventListener;

public class CampfireAuraListener implements TriggerEventListener {
    private static final float FATIGUE_DECREASE_LEVEL = 0.0003f;

    @Override
    public void onTrigger(TriggerEvent event) {
        ServerPlayerEntity player = event.player();
        if (event.triggerId().equals(ProgressionOverhaul.id("near_campfire"))) {
            FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
            fatigue.decreaseFatigue(FATIGUE_DECREASE_LEVEL);
        }
    }
}
