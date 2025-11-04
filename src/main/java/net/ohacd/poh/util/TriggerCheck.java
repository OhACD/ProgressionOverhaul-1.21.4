package net.ohacd.poh.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface TriggerCheck {
    boolean shouldTrigger(PlayerEntity player, World world);
}
