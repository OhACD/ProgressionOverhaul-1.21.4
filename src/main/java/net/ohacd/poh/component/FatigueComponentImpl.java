package net.ohacd.poh.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ohacd.poh.component.events.FatigueEvents;
import net.ohacd.poh.component.events.handlers.FatigueExhaustionHandler;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FatigueComponentImpl implements FatigueComponent, AutoSyncedComponent {
    public float fatigue = 0;

    public float valueCheck(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public float getFatigue() {
        return this.fatigue;
    }

    @Override
    public void setFatigue(float value) {
        this.fatigue = value;
    }

    @Override
    public void increaseFatigue(float amount) {
        this.fatigue = valueCheck(this.fatigue + amount, 0.0f, 1.0f);
    }

    @Override
    public void decreaseFatigue(float amount) {
        this.fatigue = valueCheck(this.fatigue - amount, 0.0f, 1.0f);
    }

    @Override
    public void tick(ServerPlayerEntity player) {


        FatigueExhaustionHandler.tick(player, this);
        FatigueEvents.applyFatigueEvents(player);

//        if (player.age % 100 == 0) {
//            increaseFatigue(0.02f);
//        }
//
//        if (getFatigue() >= 0.5f) {
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
//
//            ProgressionOverhaul.LOGGER.info("Your Fatigue is " + fatigue);
//        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        fatigue = nbtCompound.getFloat("fatigue");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putFloat("fatigue", fatigue);
    }
}
