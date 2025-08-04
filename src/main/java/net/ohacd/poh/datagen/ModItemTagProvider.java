package net.ohacd.poh.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.PEBBLES_TAG)
                .add(ModItems.PEBBLES)
                .add(ModItems.ANDESITE_PEBBLES)
                .add(ModItems.GRANITE_PEBBLES)
                .add(ModItems.DIORITE_PEBBLES);

        getOrCreateTagBuilder(ModTags.Items.CLAY_FUEL)
                .add(ModItems.BARK)
                .add(Items.STICK)
                .add(Items.BAMBOO)
                .add(Items.BIRCH_LOG)
                .add(Items.OAK_LOG)
                .add(Items.JUNGLE_LOG)
                .add(Items.ACACIA_LOG)
                .add(Items.SPRUCE_LOG)
                .add(Items.MANGROVE_LOG)
                .add(Items.PALE_OAK_LOG)
                .add(Items.DARK_OAK_LOG);

        getOrCreateTagBuilder(ModTags.Items.STRIPPED_LOGS)
                .add(Items.STRIPPED_OAK_LOG)
                .add(Items.STRIPPED_ACACIA_LOG)
                .add(Items.STRIPPED_BIRCH_LOG)
                .add(Items.STRIPPED_SPRUCE_LOG)
                .add(Items.STRIPPED_DARK_OAK_LOG)
                .add(Items.STRIPPED_PALE_OAK_LOG)
                .add(Items.STRIPPED_MANGROVE_LOG);

        getOrCreateTagBuilder(ModTags.Items.SAPLINGS_NATURAL)
                .add(Items.OAK_SAPLING)
                .add(Items.BIRCH_SAPLING)
                .add(Items.SPRUCE_SAPLING)
                .add(Items.ACACIA_SAPLING)
                .add(Items.DARK_OAK_SAPLING)
                .add(Items.CHERRY_SAPLING)
                .add(Items.JUNGLE_SAPLING)
                .add(Items.PALE_OAK_SAPLING);




    }
}
