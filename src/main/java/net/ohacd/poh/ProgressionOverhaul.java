package net.ohacd.poh;

import net.fabricmc.api.ModInitializer;

import net.ohacd.poh.block.entity.ModBLockEntity;
import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.util.ModLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressionOverhaul implements ModInitializer {
	public static final String MOD_ID = "poh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModLootTableModifiers.registerLootTables();
        ModBlocks.registerModBlocks();
        ModBLockEntity.registerBlockEntities();
	}
}