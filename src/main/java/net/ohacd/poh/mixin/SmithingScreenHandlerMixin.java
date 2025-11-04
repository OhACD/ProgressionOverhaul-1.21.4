package net.ohacd.poh.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ohacd.poh.recipe.custom.CountedSmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    private void poh$onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        SmithingScreenHandler handler = (SmithingScreenHandler) (Object) this;
        World world = player.getWorld();

        if (!(world instanceof ServerWorld serverWorld)) return;

        // Create SmithingRecipeInput manually
        SmithingRecipeInput input = new SmithingRecipeInput(
                handler.getSlot(0).getStack(), // template
                handler.getSlot(1).getStack(), // base
                handler.getSlot(2).getStack()  // addition
        );

        // Get the recipe entry
        Optional<RecipeEntry<SmithingRecipe>> recipeOpt =
                serverWorld.getRecipeManager().getFirstMatch(RecipeType.SMITHING, input, world);

        // Unwrap and handle CountedSmithingRecipe
        recipeOpt.map(RecipeEntry::value).ifPresent(recipe -> {
            if (recipe instanceof CountedSmithingRecipe counted) {
                Slot additionSlot = handler.getSlot(2);
                ItemStack additionStack = additionSlot.getStack();
                if (!additionStack.isEmpty()) {
                    additionStack.decrement(counted.getAdditionCount());
                }
            }
        });
    }
}
