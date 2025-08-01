package net.ohacd.poh.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_SHARP_TOOL = createTag("needs_sharp_tool");
        public static final TagKey<Block> INCORRECT_FOR_SHARP_TOOL = createTag("incorrect_for_sharp_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(ProgressionOverhaul.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> SHARP_TOOL_REPAIR = createTag("sharp_tool_repair");
        public static final TagKey<Item> PEBBLES_TAG = createTag("pebbles_tag");
        public static final TagKey<Item> CLAY_FUEL = createTag("clay_fuel");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressionOverhaul.MOD_ID, name));
        }
    }

    public static void registerModTags() {
        ProgressionOverhaul.LOGGER.info("Registering Mod Tags for " + ProgressionOverhaul.MOD_ID);
    }

}