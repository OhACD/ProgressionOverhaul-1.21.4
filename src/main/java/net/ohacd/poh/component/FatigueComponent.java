package net.ohacd.poh.component;

import net.minecraft.server.network.ServerPlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentV3;

public interface FatigueComponent extends ComponentV3 {
    float getFatigue();

    void setFatigue(float value);
    void increaseFatigue(float amount);
    void decreaseFatigue(float amount);
    void tick(ServerPlayerEntity player);
}
