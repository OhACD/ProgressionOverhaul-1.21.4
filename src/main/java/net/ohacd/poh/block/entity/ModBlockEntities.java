package net.ohacd.poh.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ohacd.poh.ProgressionOverhaul;
import net.ohacd.poh.block.ModBlocks;
import net.ohacd.poh.block.entity.custom.ClayFurnaceBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<ClayFurnaceBlockEntity> CLAY_FURNACE_BE =
            register("clay_furnace", ClayFurnaceBlockEntity::new, ModBlocks.CLAY_FURNACE);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(ProgressionOverhaul.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void registerBlockEntities() {
        ProgressionOverhaul.LOGGER.info("Registering Block Entities for " + ProgressionOverhaul.MOD_ID);
    }
}
