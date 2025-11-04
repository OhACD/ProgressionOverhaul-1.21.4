package net.ohacd.poh.world.events;

import java.util.ArrayList;
import java.util.List;

public final class TriggerEventDispatcher {
    private static final List<TriggerEventListener> LISTENERS = new ArrayList<>();
    private TriggerEventDispatcher() {}
    public static void register(TriggerEventListener listener) { LISTENERS.add(listener); }
    public static void post(TriggerEvent event) { for (var l : LISTENERS) l.onTrigger(event); }
}
