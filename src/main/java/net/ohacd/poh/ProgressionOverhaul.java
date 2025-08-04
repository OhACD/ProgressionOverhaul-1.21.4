package net.ohacd.poh;

import net.fabricmc.api.ModInitializer;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressionOverhaul implements ModInitializer {
	public static final String MOD_ID = "poh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
	}
}