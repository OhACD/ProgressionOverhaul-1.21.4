package net.ohacd.poh.init;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.world.WorldHooks;
import net.ohacd.poh.world.listeners.CampfireFatigueManager;
import net.ohacd.poh.world.poi.detector.BlockDetector;
import net.ohacd.poh.world.poi.detector.StructureDetector;

import java.util.Set;

public final class ModPOIDetectors {
    public static void registerAll() {
        WorldHooks.registerDetector(new BlockDetector(
                ProgressionOverhaul.MOD_ID,
                Set.of(Blocks.BELL, Blocks.CRAFTING_TABLE, Blocks.ENCHANTING_TABLE, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE)
        ));

        WorldHooks.registerDetector(StructureDetector.vanillaTargets(
                ProgressionOverhaul.MOD_ID,
                Set.of(
                        Identifier.of("minecraft:village_plains"),
                        Identifier.of("minecraft:ancient_city")
                )
        ));

        CampfireFatigueManager.register();
    }
}
