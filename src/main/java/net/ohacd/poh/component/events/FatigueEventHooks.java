package net.ohacd.poh.component.events;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.ohacd.poh.component.events.handlers.FatigueTriggerHandler;

public class FatigueEventHooks {
    public static void register() {
        AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (!world.isClient && entity instanceof LivingEntity && entity.isAlive()) {
                FatigueTriggerHandler.onCombat((ServerPlayerEntity) playerEntity);
            }
            return ActionResult.PASS;
        });

        PlayerBlockBreakEvents.AFTER.register((world, playerEntity, blockPos, blockState, blockEntity) -> {
            if (!world.isClient && playerEntity instanceof ServerPlayerEntity player) {
                FatigueTriggerHandler.onBlockBreak(player);
            }
        });
    }
}
