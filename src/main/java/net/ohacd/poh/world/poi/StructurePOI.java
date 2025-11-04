package net.ohacd.poh.world.poi;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public record StructurePOI(Identifier id,
                           RegistryKey<World> dimension,
                           Box bounds,
                           BlockPos center,
                           POIMetadata meta,
                           Identifier structureId) implements POI {}