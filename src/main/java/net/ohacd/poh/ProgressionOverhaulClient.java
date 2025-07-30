package net.ohacd.poh;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.ohacd.poh.screen.ModScreenHandlers;
import net.ohacd.poh.screen.custom.ClayFurnaceScreen;


public class ProgressionOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CLAY_FURNACE_SCREEN_HANDLER, ClayFurnaceScreen::new);
    }
}
