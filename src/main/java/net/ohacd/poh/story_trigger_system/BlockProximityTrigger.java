package net.ohacd.poh.story_trigger_system;

import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BlockProximityTrigger extends StoryProximityTrigger {
    public BlockProximityTrigger(String id, int radius, Text message, boolean oncePerLocation, Block targetBlock) {
        super(id, radius, message, oncePerLocation, (player, world) -> {
            BlockPos pos = player.getBlockPos();
            BlockPos.Mutable checkPos = new BlockPos.Mutable();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        checkPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                        if (world.getBlockState(checkPos).isOf(targetBlock)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        });
    }
}
