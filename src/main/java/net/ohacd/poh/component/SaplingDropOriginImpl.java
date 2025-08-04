package net.ohacd.poh.component;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.Random;

public class SaplingDropOriginImpl implements SaplingDropOriginComponent, ServerTickingComponent {
    private origin origin = SaplingDropOriginComponent.origin.PLAYER;
    private int tickCounter = 0;
    private static final int TICK_INTERVAL = 300;
    private static final Random RANDOM = new Random();

    private final ItemEntity holder;

    public SaplingDropOriginImpl(ItemEntity holder) {
        this.holder = holder;
    }

    @Override
    public origin getOrigin() {
        return origin;
    }

    @Override
    public void setOrigin(origin origin) {
        this.origin = origin;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (nbtCompound.contains("origin", NbtElement.STRING_TYPE)) {
            try {
                this.origin = SaplingDropOriginComponent.origin.valueOf(nbtCompound.getString("origin"));
            } catch (IllegalArgumentException e) {
                this.origin = SaplingDropOriginComponent.origin.PLAYER;
            }
        }
        tickCounter = nbtCompound.getInt("tick_counter");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putString("origin", origin.name());
        nbtCompound.putInt("tick_counter", tickCounter);
    }

    @Override
    public void serverTick() {
        if (holder == null) {
            System.out.println("[DEBUG] holder is null");
            return;
        }

        if (holder.isRemoved()) {
            System.out.println("[DEBUG] holder is removed");
            return;
        }

        if (origin != SaplingDropOriginComponent.origin.NATURAL) {
            System.out.println("[DEBUG] origin is not NATURAL, it's: " + origin);
            return;
        }

        tickCounter++;
        if (tickCounter < TICK_INTERVAL) {
            System.out.println("[DEBUG] tickCounter = " + tickCounter + ", waiting for " + TICK_INTERVAL);
            return;
        }

        tickCounter = 0;

        World world = holder.getWorld();
        BlockPos pos = holder.getBlockPos();
        ItemStack stack = holder.getStack();

        System.out.println("[DEBUG] Tick threshold met, checking stack: " + stack);

        if (!(stack.getItem() instanceof BlockItem)) {
            System.out.println("[DEBUG] Item is not a BlockItem: " + stack.getItem());
            return;
        }

        // 90% chance (for testing, consider using 1.0f for now)
        if (RANDOM.nextFloat() > 0.9f) {
            System.out.println("[DEBUG] Random chance failed");
            return;
        }

        Block block = ((BlockItem) stack.getItem()).getBlock();
        BlockPos plantingPos = pos.down();

        System.out.println("[DEBUG] Attempting to plant at " + pos + " with support block below at " + plantingPos);

        if (!block.getDefaultState().canPlaceAt(world, pos)) {
            System.out.println("[DEBUG] block cannot be placed at " + pos);
            return;
        }

        if (!world.getBlockState(plantingPos).isSolidBlock(world, plantingPos)) {
            System.out.println("[DEBUG] support block at " + plantingPos + " is not solid");
            return;
        }

        world.setBlockState(pos, block.getDefaultState());
        stack.decrement(1);
        System.out.println("[DEBUG] Planted sapling at " + pos);

        if (stack.isEmpty()) {
            holder.discard();
            System.out.println("[DEBUG] Stack empty, discarded holder");
        } else {
            holder.setStack(stack);
            System.out.println("[DEBUG] Updated holder stack to: " + stack);
        }
    }
}
