package net.ohacd.poh.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.ohacd.poh.recipe.custom.AxeCuttingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin {
    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void pohInjectCrafter(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        AxeCuttingRecipe.CURRENT_CRAFTER.set(player);
    }

    @Inject(method = "onTakeItem", at = @At("RETURN"))
    private void pohClearCrafter(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        AxeCuttingRecipe.CURRENT_CRAFTER.remove();
    }
}
