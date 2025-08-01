package net.ohacd.poh.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.recipe.custom.AxeCuttingRecipe;
import net.ohacd.poh.recipe.custom.ClayFuranceRecipe;

public class ModRecipes {
    public static final RecipeSerializer<ClayFuranceRecipe> ClAY_FURNACE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProgressionOverhaul.MOD_ID, "clay_furnace"),
                    new ClayFuranceRecipe.Serializer());
    public static final RecipeType<ClayFuranceRecipe> CLAY_FURNACE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProgressionOverhaul.MOD_ID, "clay_furnace"),
            new RecipeType<ClayFuranceRecipe>() {
                @Override
                public String toString() {
                    return "clay_furnace";
                }});

    public static final RecipeSerializer<AxeCuttingRecipe> AXE_CUTTING_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProgressionOverhaul.MOD_ID, "axe_cutting"),
            new AxeCuttingRecipe.Serializer());
    public static final RecipeType<AxeCuttingRecipe> AXE_CUTTING_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProgressionOverhaul.MOD_ID, "axe_cutting"),
            new RecipeType<AxeCuttingRecipe>() {
                @Override
                public String toString() {
                    return "axe_cutting";
                }});

    public static void registerRecipes() {
        ProgressionOverhaul.LOGGER.info("Registering Mod Recipes for " + ProgressionOverhaul.MOD_ID);
    }
}
