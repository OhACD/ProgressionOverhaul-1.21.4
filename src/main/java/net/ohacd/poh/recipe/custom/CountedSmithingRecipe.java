package net.ohacd.poh.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.recipe.ModRecipes;

import java.util.Optional;

public class CountedSmithingRecipe extends SmithingTransformRecipe {
    private final int additionCount;
    private final ItemStack result;

    public CountedSmithingRecipe(Optional<Ingredient> template, Optional<Ingredient> base,
                                 Optional<Ingredient> addition, int additionCount, ItemStack result) {
        super(template, base, addition, result);
        this.additionCount = additionCount;
        this.result = result;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, World world) {
        ProgressionOverhaul.LOGGER.info("Matches has been called successfully " + additionCount);
        return super.matches(input, world) && input.addition().getCount() >= additionCount;
    }

    @Override
    public ItemStack craft(SmithingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return result.copy();
    }

    public ItemStack getResult() {
        return result.copy();
        // Hello Curious snooper ;)
    }

    @Override
    public RecipeSerializer<SmithingTransformRecipe> getSerializer() {
        @SuppressWarnings("unchecked")
        RecipeSerializer<SmithingTransformRecipe> casted =
                (RecipeSerializer<SmithingTransformRecipe>) (Object) ModRecipes.COUNTED_SMITHING_SERIALIZER;
        return casted;
    }

    @Override
    public RecipeType<SmithingRecipe> getType() {
        return RecipeType.SMITHING;
    }

    public int getAdditionCount() {
        return additionCount;
    }

    public static class Serializer implements RecipeSerializer<CountedSmithingRecipe> {
        public static final MapCodec<CountedSmithingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Ingredient.CODEC.optionalFieldOf("template").forGetter(SmithingTransformRecipe::template),
                        Ingredient.CODEC.optionalFieldOf("base").forGetter(SmithingTransformRecipe::base),
                        Ingredient.CODEC.optionalFieldOf("addition").forGetter(SmithingTransformRecipe::addition),
                        Codec.INT.optionalFieldOf("addition_count", 1).forGetter(CountedSmithingRecipe::getAdditionCount),
                        ItemStack.CODEC.fieldOf("result").forGetter(CountedSmithingRecipe::getResult)
                ).apply(instance, CountedSmithingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, CountedSmithingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        PacketCodecs.optional(Ingredient.PACKET_CODEC), CountedSmithingRecipe::template,
                        PacketCodecs.optional(Ingredient.PACKET_CODEC), CountedSmithingRecipe::base,
                        PacketCodecs.optional(Ingredient.PACKET_CODEC), CountedSmithingRecipe::addition,
                        PacketCodecs.VAR_INT, CountedSmithingRecipe::getAdditionCount,
                        ItemStack.PACKET_CODEC, CountedSmithingRecipe::getResult,
                        CountedSmithingRecipe::new
                );

        @Override
        public MapCodec<CountedSmithingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CountedSmithingRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}