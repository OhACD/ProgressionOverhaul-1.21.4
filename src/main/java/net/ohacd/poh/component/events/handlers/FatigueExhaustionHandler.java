package net.ohacd.poh.component.events.handlers;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.component.FatigueComponentImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FatigueExhaustionHandler {
    private static final int TICK_INTERVAL = 100;
    private static final float MIN_EXHAUSTION = 0.01f;
    public static final float MAX_EXHAUSTION = 0.025f;

    private static final Map<UUID, Integer> cooldowns = new HashMap<>();
    private static final Map<UUID, Boolean> hasShownFatigueMessage = new HashMap<>();

    public static void tick(ServerPlayerEntity player, FatigueComponentImpl fatigue) {
        UUID id = player.getUuid();
        int cooldown = cooldowns.getOrDefault(id, 0);
        boolean shownMessage = hasShownFatigueMessage.getOrDefault(id, false);

        float value = fatigue.getFatigue();

        // Send the fatigue message ONCE when threshold is reached
        if (!shownMessage && value >= 0.5f) {
            player.sendMessage(Text.literal("§cYou are getting tired..."), false);
            player.sendMessage(Text.literal("§eHunger loss is increased, better rest soon"), false);
            hasShownFatigueMessage.put(id, true);
        }

        // Reset message flag if fatigue drops below threshold
        if (shownMessage && value < 0.5f) {
            hasShownFatigueMessage.put(id, false);
        }

        // Regular exhaustion logic
        if (cooldown <= 0) {
            applyExhaustion(player, fatigue);
            cooldowns.put(id, TICK_INTERVAL);
            ProgressionOverhaul.LOGGER.info("fatigue " + value);
        } else {
            cooldowns.put(id, cooldown - 1);
        }
    }

    public static void applyExhaustion(ServerPlayerEntity player, FatigueComponentImpl fatigue) {
        float value = fatigue.getFatigue();
        if (value > 0.5f) {
            float fatigueRatio = (value - 0.5f) * 2.0f;
            float exhaustion = MIN_EXHAUSTION + ((MAX_EXHAUSTION - MIN_EXHAUSTION) * fatigueRatio);
            player.getHungerManager().addExhaustion(exhaustion);
        }
    }
}