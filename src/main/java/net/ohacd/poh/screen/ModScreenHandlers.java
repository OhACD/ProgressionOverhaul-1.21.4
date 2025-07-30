package net.ohacd.poh.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.screen.custom.ClayFurnaceScreenHandler;

public class ModScreenHandlers {

    public static final ScreenHandlerType<ClayFurnaceScreenHandler> CLAY_FURNACE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(ProgressionOverhaul.MOD_ID, "clay_furnace_screen_handler"),
                    new ExtendedScreenHandlerType<>(ClayFurnaceScreenHandler::new, BlockPos.PACKET_CODEC));


    public static void registerScreenHandlers() {
        ProgressionOverhaul.LOGGER.info("Registering Screen Handlers for " + ProgressionOverhaul.MOD_ID);
    }
}
