package net.ohacd.poh.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.item.ModItems;

public class ModLootTableModifiers {
    private static final Identifier SHORT_GRASS_ID = Identifier.of("minecraft", "blocks/short_grass");
    private static final Identifier GRASS_ID = Identifier.of("minecraft", "blocks/grass_block");
    private static final Identifier DIRT_ID = Identifier.of("minecraft", "blocks/dirt");
    private static final Identifier MUD_ID = Identifier.of("minecraft", "blocks/mud");
    private static final Identifier GRAVEL_ID = Identifier.of("minecraft", "blocks/gravel");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if(SHORT_GRASS_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1F))
                        .with(ItemEntry.builder(ModItems.DRY_GRASS))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if(GRASS_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1F))
                        .with(ItemEntry.builder(ModItems.PEBBLES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if(DIRT_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1F))
                        .with(ItemEntry.builder(ModItems.PEBBLES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if(MUD_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05F))
                        .with(ItemEntry.builder(ModItems.PEBBLES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if(GRAVEL_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2F))
                        .with(ItemEntry.builder(ModItems.PEBBLES))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
    }

    public static void replaceLootTables() {
//        LootTableEvents.REPLACE.register((registryKey,
//                                          lootTable,
//                                          lootTableSource,
//                                          wrapperLookup) -> {
//            if (STONE_ID.equals(registryKey.getValue())) {
//                LootPool.Builder poolbuilder = LootPool.builder()
//                        .rolls(ConstantLootNumberProvider.create(1))
//                        .conditionally(RandomChanceLootCondition.builder(1.0F))
//                        .with(ItemEntry.builder(ModItems.PEBBLES))
//                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 5.0F)).build());
//                LootTable.Builder tableBuilder = LootTable.builder()
//                        .pool(poolbuilder);
//
//                return tableBuilder.build();
//            }
//            return lootTable;
//        });
    }

    public static void registerLootTables() {
        modifyLootTables();
        replaceLootTables();
        ProgressionOverhaul.LOGGER.info("Registering Mod Recipes for " + ProgressionOverhaul.MOD_ID);
    }
}
