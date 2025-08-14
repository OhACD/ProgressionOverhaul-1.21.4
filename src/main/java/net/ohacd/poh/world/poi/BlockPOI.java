package net.ohacd.poh.world.poi;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public record BlockPOI(Identifier id,
                       RegistryKey<World> dimension,
                       BlockPos pos,
                       POIMetadata meta,
                       Block block) implements POI {
    @Override
    public Box bounds() {
        return new Box(pos);
    }

    @Override
    public BlockPos center() {
        return pos;
    }
}
