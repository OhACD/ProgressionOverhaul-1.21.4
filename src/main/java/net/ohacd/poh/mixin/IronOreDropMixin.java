package net.ohacd.poh.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class IronOreDropMixin {

    @Inject(
        method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onDropStacks(BlockState state, World world, BlockPos pos,
                                     @Nullable BlockEntity blockEntity, @Nullable Entity entity,
                                     ItemStack tool, CallbackInfo ci) {
    	
    	if (!state.isOf(Blocks.IRON_ORE)) {
    	    return; // Targets Iron Ore only
    	}
    	
    	ci.cancel(); // Cancel vanilla drops

    	boolean validTool = tool.getItem() == Items.IRON_PICKAXE // Gives the player the ore if mined with the correct tool
                || tool.getItem() == Items.DIAMOND_PICKAXE		// pushing back the player to pre 1.18 ore to ingot system
                || tool.getItem() == Items.NETHERITE_PICKAXE;

    	if (validTool) {
    		Block.dropStack(world, pos, new ItemStack(state.getBlock().asItem()));
    	} else {
    		int chunkCount = 1 + world.getRandom().nextInt(3); // Scuffed way to make ore drop iron chunks for the time being needs changing
    		Block.dropStack(world, pos, new ItemStack(Items.RAW_IRON, chunkCount)); // Ore drops raw chunks when mined with a lower tool (smelting recipe will be changed)
}
        
    }

}
