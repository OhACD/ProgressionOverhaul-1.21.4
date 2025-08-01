package net.ohacd.poh.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.ohacd.poh.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {

                createShaped(RecipeCategory.MISC, ModBlocks.CLAY_FURNACE)
                        .pattern("RRR")
                        .pattern("R R")
                        .pattern("RRR")
                        .input('R', Items.BRICK)
                        .criterion(hasItem(Items.CLAY), conditionsFromItem(ModBlocks.CLAY_FURNACE))
                        .offerTo(exporter);

            }
        };
    }

    @Override
    public String getName() {
        return "Progression Overhaul Recipes";
    }
}
