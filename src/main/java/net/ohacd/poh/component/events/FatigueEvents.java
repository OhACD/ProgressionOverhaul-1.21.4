package net.ohacd.poh.component.events;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;

public class FatigueEvents {

    public static void applyFatigueEvents(ServerPlayerEntity player) {
        FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
        float level = fatigue.getFatigue();

        if (level >= 0.9) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200, 0));
        }

        if (level >= 0.8) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 0));

        }

        if (level > 0.5f) {
            player.removeStatusEffect(StatusEffects.SPEED);
        }

        if (level <= 0.2f) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED, 200, 0));
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE, 200, 0));
        }
    }
}
