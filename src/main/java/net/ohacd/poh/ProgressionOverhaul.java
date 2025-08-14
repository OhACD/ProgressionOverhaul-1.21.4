package net.ohacd.poh;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.block.entity.ModBlockEntities;
import net.ohacd.poh.command.ModCommands;
import net.ohacd.poh.component.events.FatigueEventHooks;
import net.ohacd.poh.component.events.handlers.FatigueTriggerHandler;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.recipe.ModRecipes;
import net.ohacd.poh.screen.ModScreenHandlers;
import net.ohacd.poh.util.ModTickHandlers;
import net.ohacd.poh.util.ModLootTableModifiers;
import net.ohacd.poh.world.WorldHooks;
import net.ohacd.poh.world.events.TriggerEvent;
import net.ohacd.poh.world.events.TriggerEventDispatcher;
import net.ohacd.poh.world.events.TriggerEventListener;
import net.ohacd.poh.world.events.TriggerEventType;
import net.ohacd.poh.world.poi.detector.BlockDetector;
import net.ohacd.poh.world.poi.detector.Detector;
import net.ohacd.poh.world.poi.detector.StructureDetector;
import net.ohacd.poh.world.triggers.LocationTriggerManager;
import net.ohacd.poh.world.triggers.impl.BlockProximityTrigger;
import net.ohacd.poh.world.triggers.impl.StructureTrigger;
import net.ohacd.poh.world.triggers.impl.ZoneTrigger;
import net.ohacd.poh.world.triggers.zones.TriggerZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;

public class ProgressionOverhaul implements ModInitializer {
	public static final String MOD_ID = "poh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	// One global manager for all triggers
	private static final LocationTriggerManager TRIGGER_MANAGER = new LocationTriggerManager();

	public static Identifier id(String path) { return Identifier.of(MOD_ID, path); }

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModCommands.register();

        ModLootTableModifiers.registerLootTables();
		ModRecipes.registerRecipes();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();


		ModTickHandlers.register();
		FatigueTriggerHandler.init();
		FatigueEventHooks.register();


		// Install world hooks (detectors + block/structure indexing)
		WorldHooks.init();

		// Register detectors: which blocks & structures should become POIs?
		WorldHooks.registerDetector(new BlockDetector(
				MOD_ID,
				Set.of(Blocks.BELL, Blocks.CRAFTING_TABLE, Blocks.ENCHANTING_TABLE)
		));
		WorldHooks.registerDetector(StructureDetector.vanillaTargets(MOD_ID, Set.of(
                Identifier.of("minecraft:village_plains"),
                Identifier.of("minecraft:ancient_city")
		)));

		// Wire the trigger manager to the server tick
		ServerTickEvents.END_SERVER_TICK.register(TRIGGER_MANAGER);

		// Example triggers so you can test immediately
		TRIGGER_MANAGER.register(new BlockProximityTrigger(id("near_bell"), Set.of(Blocks.BELL), 6));

		TRIGGER_MANAGER.register(new StructureTrigger(
				id("near_plains_village"),
				Identifier.of("minecraft:village_plains"),
				96.0 // radius in blocks around structure center OR inside its bounds
		));

		TRIGGER_MANAGER.register(new ZoneTrigger(new TriggerZone(
				id("spawn_square"),
				// dimension is set at runtime below when a player first joins overworld; leave null here
				null, // we’ll fix dimension at first event via listener; see below
				new Box(new BlockPos(-32, -64, -32), new BlockPos(32, 384, 32))
		)));

		// Listen to trigger events and show quick chat messages for testing
		TriggerEventDispatcher.register(new TriggerEventListener() {
			@Override public void onTrigger(TriggerEvent e) {
				String kind = switch (e.type()) {
					case ENTER_STRUCTURE -> "ENTER_STRUCTURE";
					case NEAR_BLOCK -> "NEAR_BLOCK";
					case ENTER_ZONE -> "ENTER_ZONE";
				};
				var p = e.player();
				var at = e.pos() != null ? " @" + e.pos().toShortString() : "";
				p.sendMessage(Text.literal("[Trigger] " + kind + " -> " + e.subjectId() + at), false);
			}
		});

		// Small helper: ensure ZoneTrigger’s dimension is set (since our example used null)
		TriggerEventDispatcher.register(e -> {
			if (e.type() == TriggerEventType.ENTER_ZONE) {
				// nothing to do; the example ZoneTrigger checks dimension via player world anyway
			}
		});
	}
}