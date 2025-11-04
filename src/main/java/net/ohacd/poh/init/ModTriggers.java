package net.ohacd.poh.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.world.events.TriggerEventDispatcher;
import net.ohacd.poh.world.listeners.CampfireAuraListener;
import net.ohacd.poh.world.triggers.LocationTriggerManager;
import net.ohacd.poh.world.triggers.impl.BlockProximityTrigger;
import net.ohacd.poh.world.triggers.impl.StructureTrigger;
import net.ohacd.poh.world.triggers.impl.ZoneTrigger;
import net.ohacd.poh.world.triggers.zones.TriggerZone;

import java.util.Set;

public final class ModTriggers {
    private static final LocationTriggerManager MANAGER = new LocationTriggerManager();

    public static void registerAll() {
        ServerTickEvents.END_SERVER_TICK.register(MANAGER);

        MANAGER.register(new BlockProximityTrigger(
                ProgressionOverhaul.id("near_bell"),
                Set.of(Blocks.BELL),
                6
        ));

        MANAGER.register(new BlockProximityTrigger(
                ProgressionOverhaul.id("near_campfire"),
                Set.of(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE),
                12,
                state -> state.get(CampfireBlock.LIT)
        ).withMovement(false));

        MANAGER.register(new StructureTrigger(
                ProgressionOverhaul.id("near_plains_village"),
                Identifier.of("minecraft:village_plains"),
                96.0
        ));

        MANAGER.register(new ZoneTrigger(new TriggerZone(
                ProgressionOverhaul.id("spawn_square"),
                null,
                new Box(
                        new BlockPos(-32, -64, -32).toCenterPos(),
                        new BlockPos(32, 384, 32).toCenterPos()
                )        )));


        // Registering Triggers
        TriggerEventDispatcher.register(new CampfireAuraListener());

    }
}
