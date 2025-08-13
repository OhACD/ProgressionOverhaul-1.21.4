package net.ohacd.poh.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.util.ModTags;

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

                createShapeless(RecipeCategory.TOOLS, ModItems.SHARP_STONE, 1)
                        .input(ModTags.Items.PEBBLES_TAG)
                        .input(ModTags.Items.PEBBLES_TAG)
                                .criterion(hasItem(ModItems.PEBBLES), conditionsFromItem(ModItems.PEBBLES))
                                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, ModBlocks.CLAY_FURNACE)
                        .pattern("RRR")
                        .pattern("R R")
                        .pattern("RRR")
                        .input('R', Items.BRICK)
                        .criterion(hasItem(Items.CLAY), conditionsFromItem(ModBlocks.CLAY_FURNACE))
                        .offerTo(exporter);

//                createShapeless(RecipeCategory.MISC, Items.STICK, 1)
//                        .input(ModTags.Items.SAPLINGS_NATURAL)
//                        .criterion(hasItem(Items.OAK_SAPLING), conditionsFromItem(Items.STICK))
//                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, Items.STONE_PICKAXE)
                        .pattern("CCC")
                        .pattern(" SF")
                        .pattern(" S ")
                        .input('C', ItemTags.STONE_TOOL_MATERIALS)
                        .input('S', Items.STICK)
                        .input('F', Items.STRING)
                        .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.STONE_PICKAXE))
                        .offerTo(exporter);




            }
        };
    }

    @Override
    public String getName() {
        return "Progression Overhaul Recipes";
    }
}
