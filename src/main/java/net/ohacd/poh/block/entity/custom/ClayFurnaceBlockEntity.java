package net.ohacd.poh.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ohacd.poh.block.entity.ImplementedInventory;
import net.ohacd.poh.block.entity.ModBlockEntities;
import net.ohacd.poh.item.ModItems;
import net.ohacd.poh.recipe.ModRecipes;
import net.ohacd.poh.recipe.custom.ClayFuranceRecipe;
import net.ohacd.poh.recipe.custom.ClayFurnaceRecipeInput;
import net.ohacd.poh.screen.custom.ClayFurnaceScreenHandler;
import net.ohacd.poh.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class ClayFurnaceBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 300;

    private int burnTime = 0;
    private int fuelTime = 0;

    public ClayFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLAY_FURNACE_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> burnTime;
                    case 3 -> fuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                    case 2 -> burnTime = value;
                    case 3 -> fuelTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.poh.clay_furnace");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ClayFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("clay_furnace.progress", progress);
        nbt.putInt("clay_furnace.max_progress", maxProgress);
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("fuelTime", fuelTime);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        progress = nbt.getInt("clay_furnace.progress");
        maxProgress = nbt.getInt("clay_furnace.maxProgress");
        burnTime = nbt.getInt("burnTime");
        fuelTime = nbt.getInt("fuelTime");
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (burnTime > 0) {
            burnTime--;
        }

        if (hasRecipe()) {
            if (burnTime > 0) {
                increaseCraftingProgress();
            } else if (canConsumeFuel()) {
                consumeFuel();
            }

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }

        markDirty(world, pos, state);
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<ClayFuranceRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();

        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeEntry<ClayFuranceRecipe>> getCurrentRecipe() {
        return ((ServerWorld) this.getWorld()).getRecipeManager()
                .getFirstMatch(ModRecipes.CLAY_FURNACE_TYPE, new ClayFurnaceRecipeInput(inventory.get(INPUT_SLOT)),
                        this.getWorld());
    }

//    //List of Items that can be used as fuel
//    private static final Set<Item> FUEL_ITEMS = Set.of(
//            ModItems.BARK,
//            Items.STICK,
//            Items.BAMBOO,
//            Items.BIRCH_LOG,
//            Items.OAK_LOG,
//            Items.JUNGLE_LOG,
//            Items.ACACIA_LOG,
//            Items.SPRUCE_LOG,
//            Items.MANGROVE_LOG,
//            Items.PALE_OAK_LOG
//
//
//
//    );


    private void consumeFuel() {
        ItemStack fuelStack = getStack(FUEL_SLOT);
//        Item fuelItem = fuelStack.getItem();

        if (fuelStack.isIn(ModTags.Items.CLAY_FUEL)) {
            burnTime = fuelTime = 150;
            fuelStack.decrement(1);
        }
    }

    private boolean canConsumeFuel() {
        ItemStack fuelStack = getStack(FUEL_SLOT);
        return fuelStack.isIn(ModTags.Items.CLAY_FUEL);
    }

    private int getFuelTime(ItemStack stack) {
        return stack.isIn(ModTags.Items.CLAY_FUEL) ? 150 : 0;
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 300;
    }

    private void craftItem() {
        Optional<RecipeEntry<ClayFuranceRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();

        this.removeStack(INPUT_SLOT, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));

    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}