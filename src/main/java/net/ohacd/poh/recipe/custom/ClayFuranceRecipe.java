package net.ohacd.poh.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ohacd.poh.recipe.ModRecipes;

public record ClayFuranceRecipe(Ingredient inputItem, ItemStack output) implements Recipe<ClayFurnaceRecipeInput> {

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }
    
    @Override
    public boolean matches(ClayFurnaceRecipeInput input, World world) {
        if (world.isClient) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(ClayFurnaceRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<ClayFurnaceRecipeInput>> getSerializer() {
        return ModRecipes.ClAY_FURNACE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<ClayFurnaceRecipeInput>> getType() {
        return ModRecipes.CLAY_FURNACE_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(inputItem);
    }


    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<ClayFuranceRecipe> {
        public static final MapCodec<ClayFuranceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ClayFuranceRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(ClayFuranceRecipe::output)
        ).apply(inst, ClayFuranceRecipe::new));

        public static final PacketCodec<RegistryByteBuf, ClayFuranceRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, ClayFuranceRecipe::inputItem,
                        ItemStack.PACKET_CODEC, ClayFuranceRecipe::output,
                        ClayFuranceRecipe::new);

        @Override
        public MapCodec<ClayFuranceRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ClayFuranceRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
