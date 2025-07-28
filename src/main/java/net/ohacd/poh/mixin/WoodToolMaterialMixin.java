package net.ohacd.poh.mixin;

import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//Mixin doesn't work, ToolMaterial class is now a record :(
@Mixin(ToolMaterial.class)
public class WoodToolMaterialMixin {
    @Shadow @Final public static ToolMaterial WOOD;

    @Inject(method = "durability", at = @At("RETURN"), cancellable = true)
    private void injectedDurability(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this == WOOD) {
            cir.setReturnValue(16);
        }
    }
}
