package net.ohacd.poh;

import net.fabricmc.api.ModInitializer;

import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.block.entity.ModBlockEntities;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.screen.ModScreenHandlers;
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

        ModLootTableModifiers.registerLootTables();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
	}
}