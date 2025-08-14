package net.ohacd.poh.world.events;

@FunctionalInterface
public interface TriggerEventListener {
    void onTrigger(TriggerEvent event);
}
