package net.ohacd.poh.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.block.entity.custom.ClayFurnaceBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<ClayFurnaceBlockEntity> CLAY_FURNACE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(ProgressionOverhaul.MOD_ID, "clay_furnace_be"),
                    FabricBlockEntityTypeBuilder.create(ClayFurnaceBlockEntity::new, ModBlocks.CLAY_FURNACE).build(null));


    public static void registerBlockEntities() {
        ProgressionOverhaul.LOGGER.info("Registering Block Entities for " + ProgressionOverhaul.MOD_ID);
    }
}
