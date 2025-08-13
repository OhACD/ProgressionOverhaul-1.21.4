package net.ohacd.poh.story_trigger_system;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.ohacd.poh.util.TriggerCheck;

public class PoiProximityTrigger extends StoryProximityTrigger {

    public PoiProximityTrigger(String id, int radius, Text message, boolean oncePerLocation, TriggerCheck triggerCheck) {
        super(id, radius, message, oncePerLocation, triggerCheck);
    }

    public PoiProximityTrigger(
            String id,
            int radius,
            Text message,
            boolean oncePerLocation,
            RegistryKey<PointOfInterestType> poiKey
    ) {
        super(
                id,
                radius,
                message,
                oncePerLocation,
                (player, world) -> {
                    if (!(world instanceof ServerWorld serverWorld)) {
                        return false;
                    }
                    BlockPos pos = player.getBlockPos();
                    return serverWorld.getPointOfInterestStorage()
                            .getInSquare(
                                    entry -> entry.matchesKey(poiKey),
                                    pos,
                                    radius,
                                    PointOfInterestStorage.OccupationStatus.ANY
                            )
                            .findAny()
                            .isPresent();
                }
        );
    }
}
