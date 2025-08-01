package net.ohacd.poh.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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

    }
}
