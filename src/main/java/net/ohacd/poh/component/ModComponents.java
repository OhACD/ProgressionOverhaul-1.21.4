package net.ohacd.poh.component;

import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<FatigueComponent> FATIGUE =
            ComponentRegistry.getOrCreate(Identifier.of(ProgressionOverhaul.MOD_ID, "fatigue"), FatigueComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(FATIGUE, playerEntity -> new FatigueComponentImpl(),
                RespawnCopyStrategy.ALWAYS_COPY);
    }
}
