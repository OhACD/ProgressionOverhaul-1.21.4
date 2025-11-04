package net.ohacd.poh.component;

import net.minecraft.util.math.BlockPos;
import org.ladysnake.cca.api.v3.component.ComponentV3;

public interface PlayerStoryData extends ComponentV3 {
    boolean hasTriggered(String id);
    boolean hasTriggeredAt(String id, BlockPos location, double minDistanceSq);
    void trigger(String id, BlockPos location, boolean perLocation);
}
