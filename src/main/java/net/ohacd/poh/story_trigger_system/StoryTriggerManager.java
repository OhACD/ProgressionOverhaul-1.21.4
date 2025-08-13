package net.ohacd.poh.story_trigger_system;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterestTypes;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.component.PlayerStoryData;

import java.util.ArrayList;
import java.util.List;

public class StoryTriggerManager {
    private static final List<StoryProximityTrigger> TRIGGERS = new ArrayList<>();
    private static final double SAME_LOCATION_DISTANCE_SQ = 32 * 32;

    public static void registerDefaults() {
        TRIGGERS.add(new PoiProximityTrigger(
                "blacksmith_hint",
                64,
                Text.literal("A skilled blacksmith might reside in this village!"),
                true,
                PointOfInterestTypes.ARMORER
        ));

        TRIGGERS.add(new BlockProximityTrigger(
                "campfire_hint",
                8,
                Text.literal("Your skin is feeling the warmth of a campfire, You are starting to feel rested."),
                false,
                Blocks.CAMPFIRE
        ));
    }

    public static void tick(ServerPlayerEntity player) {
        PlayerStoryData data = ModComponents.STORY_DATA.get(player);
        for (StoryProximityTrigger trigger : TRIGGERS) {
            if (trigger.getCondition().shouldTrigger(player, player.getWorld())) {
                BlockPos pos = player.getBlockPos();

                if (trigger.isOncePerLocation()) {
                    if (!data.hasTriggeredAt(trigger.getId(), pos, SAME_LOCATION_DISTANCE_SQ)) {
                        player.sendMessage(trigger.getMessage(), false);
                        data.trigger(trigger.getId(), pos, true);
                    }
                } else {
                    if (!data.hasTriggered(trigger.getId())) {
                        player.sendMessage(trigger.getMessage(), false);
                        data.trigger(trigger.getId(), pos, false);
                    }
                }
            }
        }
    }

    private static boolean isNearBlock(PlayerEntity player, Block block, int radius) {
        BlockPos playerPos = player.getBlockPos();
        for (BlockPos pos : BlockPos.iterateOutwards(playerPos, radius, radius, radius)) {
            if (player.getWorld().getBlockState(pos).isOf(block)) {
                return true;
            }
        }
        return false;
    }

//    private static boolean isNearStructure(PlayerEntity player, RegistryKey<Structure> structureKey, int radius) {
//        return player.getWorld().getStr
//    }
}

