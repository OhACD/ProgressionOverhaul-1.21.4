package net.ohacd.poh.init;

import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.block.entity.ModBlockEntities;
import net.ohacd.poh.command.ModCommands;
import net.ohacd.poh.component.events.FatigueEventHooks;
import net.ohacd.poh.component.events.handlers.FatigueTriggerHandler;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.recipe.ModRecipes;
import net.ohacd.poh.screen.ModScreenHandlers;
import net.ohacd.poh.util.ModLootTableModifiers;
import net.ohacd.poh.util.ModTickHandlers;
import net.ohacd.poh.world.WorldHooks;
import net.ohacd.poh.world.listeners.CampfireFatigueManager;

public final class ModRegistries {
    public static void registerAll() {
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
        WorldHooks.init();

    }
}
