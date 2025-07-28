package net.ohacd.poh.item;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.material.ModToolMaterial;

import java.util.function.Function;

public class ModItems {
    //Items Registry
    public static final Item PLANT_FIBER = registerItem("plant_fiber", Item::new, new Item.Settings());
    public static final Item DRY_GRASS = registerItem("dry_grass", Item::new, new Item.Settings());
    public static final Item PEBBLES = registerItem("pebbles", Item::new, new Item.Settings());
    public static final Item GRANITE_PEBBLES = registerItem("granite_pebbles", Item::new, new Item.Settings());
    public static final Item DIORITE_PEBBLES = registerItem("diorite_pebbles", Item::new, new Item.Settings());
    public static final Item ANDESITE_PEBBLES = registerItem("andesite_pebbles.json", Item::new, new Item.Settings());
    public static final Item BARK = registerItem("bark", Item::new, new Item.Settings());
    public static final Item LOOSE_DIRT = registerItem("loose_dirt", Item::new, new Item.Settings());

    //Tools Registry
    public static final Item SHARP_STONE = registerItem(
            "sharp_stone",
            settings -> new PickaxeItem(ModToolMaterial.SHARP_TOOL_MATERIAL, 0.5f, -1.8f, settings),
            new Item.Settings()
    );

    private static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProgressionOverhaul.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void registerModItems(){
        ProgressionOverhaul.LOGGER.info("Registering Mod Items for" + ProgressionOverhaul.MOD_ID);
    }
}


