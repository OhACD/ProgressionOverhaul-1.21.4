package net.ohacd.poh.screen.custom;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.block.entity.custom.ClayFurnaceBlockEntity;
import net.ohacd.poh.screen.ModScreenHandlers;

public class ClayFurnaceScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final ClayFurnaceBlockEntity blockEntity;

    public ClayFurnaceScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this((ClayFurnaceBlockEntity) inventory.player.getWorld().getBlockEntity(pos), syncId, inventory);
    }

    public ClayFurnaceScreenHandler(ClayFurnaceBlockEntity blockEntity, int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, blockEntity, blockEntity.getPropertyDelegate());
    }

    public ClayFurnaceScreenHandler(int syncId, PlayerInventory playerInventory,
                                    ClayFurnaceBlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.CLAY_FURNACE_SCREEN_HANDLER, syncId);
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        this.propertyDelegate = propertyDelegate;

        // Furnace slots
        this.addSlot(new Slot(inventory, 0, 56, 17)); // Input
        this.addSlot(new Slot(inventory, 1, 56, 53)); // Fuel
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35)); // Output

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1); // Max Progress
        int arrowPixelSize = 24; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
