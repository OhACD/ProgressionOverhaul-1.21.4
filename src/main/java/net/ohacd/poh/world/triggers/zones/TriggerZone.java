package net.ohacd.poh.world.triggers.zones;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public record TriggerZone(Identifier id,
                          RegistryKey<World> dimension,
                          Box box) {
    public boolean contains(ServerPlayerEntity player) {
        return player.getWorld().getRegistryKey().equals(dimension) && box.contains(player.getPos());
    }
}
