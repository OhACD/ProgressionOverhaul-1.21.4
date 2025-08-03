package net.ohacd.poh.component.events.handlers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BedBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;

public class FatigueTriggerHandler {
    private static final int REST_TICK_THRESHOLD = 2400;
    private static final float FATIGUE_INCREASE_AMOUNT = 0.0005f;
    private static final float FATIGUE_COMBAT_AMOUNT = 0.01f;
    private static final float FATIGUE_BLOCK_BREAK_AMOUNT = 0.005f;


    public static void init() {
        registerSleepReset();
        registerWorldTickFatigue();
    }
        // Resets Fatigue on player sleep
    private static void registerSleepReset() {
        UseBlockCallback.EVENT.register(((playerEntity, world, hand, blockHitResult) -> {
            if (!world.isClient) {
                BlockPos pos = blockHitResult.getBlockPos();
                if (world.getBlockState(pos).getBlock() instanceof BedBlock) {
                    FatigueComponent fatigue = ModComponents.FATIGUE.get(playerEntity);
                    fatigue.setFatigue(0f);
                    playerEntity.sendMessage(Text.literal("You are no longer tired"), false);
                    ProgressionOverhaul.LOGGER.info("fatigue reset " + fatigue.getFatigue());
                }
            }
            return ActionResult.PASS;
        }));
    }

    private static void registerWorldTickFatigue() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                FatigueComponent fatigue = ModComponents.FATIGUE.get(player);

                // Passive fatigue increase every 20 ticks (1 second)
                if (player.age % 20 == 0) {
                    fatigue.increaseFatigue(FATIGUE_INCREASE_AMOUNT); // tweak this rate as needed
                }

                // Debug log every 5 seconds
                if (player.age % 100 == 0) {
                    ProgressionOverhaul.LOGGER.info("Fatigue for " + player.getName().getString() + ": " + fatigue.getFatigue());
                }
            }
        });

        // Add more triggers here

    }

    public static void onCombat(ServerPlayerEntity player) {
        if (!player.getWorld().isClient) {
            FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
            fatigue.increaseFatigue(FATIGUE_COMBAT_AMOUNT);
        }
    }

    public static void onBlockBreak(ServerPlayerEntity player) {
        if (!player.getWorld().isClient) {
            FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
            fatigue.increaseFatigue(FATIGUE_BLOCK_BREAK_AMOUNT);
        }
    }
}
