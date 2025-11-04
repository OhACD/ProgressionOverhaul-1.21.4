package net.ohacd.poh.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.block.custom.ClayFurnaceBlock;

import java.util.function.Function;

public class ModBlocks {

    public static final Block CLAY_FURNACE = registerBlock("clay_furnace",
            properties -> new ClayFurnaceBlock(properties.strength(3.5F)));

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ProgressionOverhaul.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(Registries.BLOCK, Identifier.of(ProgressionOverhaul.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(ProgressionOverhaul.MOD_ID, name),
                new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProgressionOverhaul.MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        ProgressionOverhaul.LOGGER.info("Registering Mod Blocks For " + ProgressionOverhaul.MOD_ID);
    }
}
