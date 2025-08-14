package net.ohacd.poh;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import net.ohacd.poh.init.ModPOIDetectors;
import net.ohacd.poh.init.ModRegistries;
import net.ohacd.poh.init.ModTriggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressionOverhaul implements ModInitializer {
	public static final String MOD_ID = "poh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier id(String path) { return Identifier.of(MOD_ID, path); }

	@Override
	public void onInitialize() {
		ModRegistries.registerAll();    // items, blocks, recipes, etc.
		ModPOIDetectors.registerAll();  // detectors for POI indexing
		ModTriggers.registerAll();
	}
}