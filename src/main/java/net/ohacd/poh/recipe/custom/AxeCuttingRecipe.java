package net.ohacd.poh.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.recipe.ModRecipes;
import net.ohacd.poh.util.ModTags;

public record AxeCuttingRecipe(Ingredient inputItem, Ingredient axe, ItemStack output) implements CraftingRecipe {
    public static final ThreadLocal<PlayerEntity> CURRENT_CRAFTER = new ThreadLocal<>();

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        list.add(this.axe);
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
                if (foundInput) {
                    return false;
                }
                foundInput = true;
            } else if (this.axe.test(stack)) {
                if (foundAxe) {
                    return false;
                }
                foundAxe = true;
            } else {
                return false;
            }
        }

        return foundInput && foundAxe;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        PlayerEntity player = CURRENT_CRAFTER.get();
        if (player != null && !player.getWorld().isClient) {
            // Check if we’re crafting stripped logs
            if (output.isIn(ModTags.Items.STRIPPED_LOGS)) {
                int amount = player.getRandom().nextBetween(1, 3);
                ItemStack bark = new ItemStack(ModItems.BARK, amount);
                ItemEntity drop = new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), bark);
                player.getWorld().spawnEntity(drop);
            }
        }

        return output.copy();
    }

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        int size = input.getWidth() * input.getHeight();
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(size, ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
            ItemStack stack = input.getStackInSlot(i);

            if (this.axe.test(stack)) {
                ItemStack damaged = stack.copy();

                // Damage the item by 1 durability
                if (damaged.isDamageable()) {
                    damaged.setDamage(damaged.getDamage() + 1);

                    if (damaged.getDamage() >= damaged.getMaxDamage()) {
                        // Axe broke
                        remainders.set(i, ItemStack.EMPTY);
                    } else {
                        remainders.set(i, damaged);
                    }
                } else {
                    remainders.set(i, damaged);
                }
            } else {
                remainders.set(i, ItemStack.EMPTY);
            }
        }

        return remainders;
    }


    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return ModRecipes.AXE_CUTTING_SERIALIZER;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return CraftingRecipeCategory.MISC;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forShapeless(getIngredients());
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
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
