package net.ohacd.poh.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ohacd.poh.block.entity.custom.ClayFurnaceBlockEntity;

public class ClayFurnaceBlock extends FurnaceBlock {

    public ClayFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClayFurnaceBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ClayFurnaceBlockEntity) {
                player.openHandledScreen((ClayFurnaceBlockEntity) blockEntity);
            }
        }
    }
}
