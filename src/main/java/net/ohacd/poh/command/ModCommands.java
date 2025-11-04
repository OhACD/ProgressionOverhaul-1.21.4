package net.ohacd.poh.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.ohacd.poh.command.custom.FatigueCommand;
import net.ohacd.poh.command.custom.SpawnSaplingCommand;
import net.ohacd.poh.command.custom.TestSaplingCommand;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SpawnSaplingCommand.register(dispatcher, registryAccess);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FatigueCommand.register(dispatcher, registryAccess);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TestSaplingCommand.register(dispatcher);
        });
    }
}
