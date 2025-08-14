package net.ohacd.poh.world.events;

import java.util.ArrayList;
import java.util.List;

public class TriggerEventDispatcher {
    private static final List<TriggerEventListener> LISTENERS = new ArrayList<>();

    public static void register(TriggerEventListener listener) {LISTENERS.add(listener); }
    public static void post(TriggerEvent event) {
        for (var listeners : LISTENERS) listeners.onTrigger(event);
    }
}
