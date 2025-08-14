package net.ohacd.test;

import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventListener;

public class LoggingTriggerListener implements TriggerEventListener {
    @Override
    public void onTrigger(TriggerEvent e) {
        var p = e.player();
        var msg = "[Trigger] " + e.type() + " -> " + e.subjectId();
        if (e.pos() != null) msg += " @" + e.pos().toShortString();
        p.sendMessage(net.minecraft.text.Text.literal(msg), false);
    }
}
