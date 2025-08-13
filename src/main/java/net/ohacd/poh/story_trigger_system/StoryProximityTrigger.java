package net.ohacd.poh.story_trigger_system;

import net.minecraft.text.Text;
import net.ohacd.poh.util.TriggerCheck;

public class StoryProximityTrigger {
    private final String id;
    private final int radius;
    private final Text message;
    private final boolean oncePerLocation;
    private final TriggerCheck getCondition;

    public StoryProximityTrigger(String id, int radius, Text message, boolean oncePerLocation, TriggerCheck getCondition) {
        this.id = id;
        this.radius = radius;
        this.message = message;
        this.oncePerLocation = oncePerLocation;
        this.getCondition = getCondition;
    }

    public String getId() {
        return id;
    }

    public int getRadius() {
        return radius;
    }

    public Text getMessage() {
        return message;
    }

    public boolean isOncePerLocation() {
        return oncePerLocation;
    }

    public TriggerCheck getCondition() {
        return getCondition;
    }
}
