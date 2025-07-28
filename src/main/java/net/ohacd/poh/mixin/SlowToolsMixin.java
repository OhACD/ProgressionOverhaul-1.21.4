package net.ohacd.poh.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class SlowToolsMixin {
	@Inject(method = "getMiningSpeed", at = @At("RETURN"), cancellable = true)
	private void modifyMiningSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		Item item = (Item)(Object)this;
		// Makes the player mine way slower without tools and slightly lowers the mining speed of tools
		if (!(item instanceof MiningToolItem)) {
			cir.setReturnValue(cir.getReturnValue() * 0.3f);
		}else if (item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL) {
			cir.setReturnValue(cir.getReturnValue() * 0.5f);
			}
		else if (item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL) {
			cir.setReturnValue(cir.getReturnValue() * 0.75f);
			}else if (item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL) {
				cir.setReturnValue(cir.getReturnValue() * 0.9f);
				}
	}
}
