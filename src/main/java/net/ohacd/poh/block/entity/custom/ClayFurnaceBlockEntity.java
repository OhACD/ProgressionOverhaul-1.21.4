package net.ohacd.poh.block.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.block.entity.ImplementedInventory;
import net.ohacd.poh.block.entity.ModBlockEntities;

public class ClayFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }

    public ClayFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLAY_FURNACE_BE, pos, state, RecipeType.SMELTING);
    }

    @Override
    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
        return super.getFuelTime(fuelRegistry, stack);
    }


    @Override
    protected Text getContainerName() {
        return Text.translatable("block.poh.clay_furnace");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
