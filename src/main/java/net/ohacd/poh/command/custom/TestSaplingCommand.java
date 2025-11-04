package net.ohacd.poh.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.component.SaplingDropOriginComponent;

public class TestSaplingCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sapling")
                .then(CommandManager.literal("test")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerWorld world = source.getWorld();
                            Vec3d pos = source.getPosition();

                            ItemEntity entity = new ItemEntity(world, pos.x, pos.y - 3, pos.z, new ItemStack(Items.OAK_SAPLING));
                            SaplingDropOriginComponent comp = ModComponents.ORIGIN.get(entity);
                            comp.setOrigin(SaplingDropOriginComponent.origin.NATURAL);
                            world.spawnEntity(entity);

                            source.sendFeedback(() -> Text.literal("Spawned sapling with NATURAL origin"), false);
                            return 1;
                        })
                )
        );
    }
}
