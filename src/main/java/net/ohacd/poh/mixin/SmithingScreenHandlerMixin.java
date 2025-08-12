package net.ohacd.poh.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.ohacd.poh.recipe.custom.CountedSmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin {

    @Inject(
            method = "onTakeOutput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/SmithingScreenHandler;decrementStack(I)V",
                    ordinal = 2 // the third decrementStack call (slot 2)
            ),
            cancellable = true
    )
    private void modifyAdditionDecrement(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        SmithingScreenHandler self = (SmithingScreenHandler) (Object) this;

        // Create a smithing recipe input from the slots
        SmithingRecipeInput input = new SmithingRecipeInput(
                self.getSlot(0).getStack(), // template
                self.getSlot(1).getStack(), // base
                self.getSlot(2).getStack()  // addition
        );

        if (player.getWorld() instanceof ServerWorld serverWorld) {
            Optional<RecipeEntry<SmithingRecipe>> optional =
                    serverWorld.getRecipeManager()
                            .getFirstMatch(RecipeType.SMITHING, input, serverWorld);

            if (optional.isPresent() && optional.get().value() instanceof CountedSmithingRecipe custom) {
                int countToRemove = custom.getAdditionCount();

                self.getSlot(2).getStack().decrement(countToRemove);
                self.getSlot(1).getStack().decrement(1);
                self.getSlot(0).getStack().decrement(1);

                ci.cancel();
            }
        }
    }
}
