package net.ohacd.poh.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.component.SaplingDropOriginComponent;
import net.ohacd.poh.util.ModTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(net.minecraft.block.Block.class)
public abstract class LeavesDropMixin {

    @Inject(
            method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onDropStacks(BlockState state, World world, BlockPos pos,
                                     @Nullable BlockEntity blockEntity, @Nullable Entity entity,
                                     ItemStack tool, CallbackInfo ci) {

        if (!(state.getBlock() instanceof LeavesBlock || world.isClient)) return;

        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, tool);

        for (ItemStack drop : drops) {
            if (drop.isIn(ModTags.Items.SAPLINGS_NATURAL)) { // checks if the entity is a sapling
                ItemEntity sapling = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, drop.copy());
                SaplingDropOriginComponent comp = ModComponents.ORIGIN.get(sapling); // replaces the sapling with my tagged sapling
                comp.setOrigin(SaplingDropOriginComponent.origin.NATURAL);
                world.spawnEntity(sapling);

            } else {
                Block.dropStack(world, pos, drop);
            }
        }

        ci.cancel();
    }

}
