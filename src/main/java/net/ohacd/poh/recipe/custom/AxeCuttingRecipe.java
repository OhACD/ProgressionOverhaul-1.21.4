package net.ohacd.poh.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ohacd.poh.recipe.ModRecipes;

public record AxeCuttingRecipe(Ingredient inputItem, Ingredient axe, ItemStack output) implements Recipe<CraftingRecipeInput> {

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (world.isClient) return false;

        boolean foundInput = false;
        boolean foundAxe = false;

        int size = input.getWidth() * input.getHeight();
        for (int i = 0; i < size; i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (this.inputItem.test(stack)) {
                if (foundInput) return false;
                foundInput = true;
            } else if (this.axe.test(stack)) {
                if (foundAxe) return false;
                foundAxe = true;
            } else {
                return false;
            }
        }

        return foundInput && foundAxe;
    }


    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<CraftingRecipeInput>> getSerializer() {
        return ModRecipes.AXE_CUTTING_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<CraftingRecipeInput>> getType() {
        return ModRecipes.AXE_CUTTING_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forShapeless(getIngredients());
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    //codecs take the order of your constructor
    public static class Serializer implements RecipeSerializer<AxeCuttingRecipe> {
        public static final MapCodec<AxeCuttingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(AxeCuttingRecipe::inputItem),
                Ingredient.CODEC.fieldOf("axe").forGetter(AxeCuttingRecipe::axe),
                ItemStack.CODEC.fieldOf("result").forGetter(AxeCuttingRecipe::output)
        ).apply(inst, AxeCuttingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, AxeCuttingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, AxeCuttingRecipe::inputItem,
                        Ingredient.PACKET_CODEC, AxeCuttingRecipe::axe,
                        ItemStack.PACKET_CODEC, AxeCuttingRecipe::output,
                        AxeCuttingRecipe::new);

        @Override
        public MapCodec<AxeCuttingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, AxeCuttingRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
