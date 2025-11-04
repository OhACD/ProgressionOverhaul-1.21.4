package net.ohacd.poh.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.ohacd.poh.component.ModComponents;
import net.ohacd.poh.component.SaplingDropOriginComponent;

public class SpawnSaplingCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("spawnsapling")
                .requires(source -> source.hasPermissionLevel(2)) // only allow OPs
                .then(CommandManager.argument("origin", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest("natural");
                            builder.suggest("player");
                            return builder.buildFuture();
                        })
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .executes(context -> {
                                    String originStr = StringArgumentType.getString(context, "origin");
                                    BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");

                                    ServerCommandSource source = context.getSource();
                                    ServerWorld world = source.getWorld();

                                    ItemStack sapling = new ItemStack(Items.OAK_SAPLING);
                                    ItemEntity saplingEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, sapling);

                                    // Set component
                                    SaplingDropOriginComponent component = ModComponents.ORIGIN.get(saplingEntity);
                                    if (originStr.equalsIgnoreCase("natural")) {
                                        component.setOrigin(SaplingDropOriginComponent.origin.NATURAL);
                                    } else {
                                        component.setOrigin(SaplingDropOriginComponent.origin.PLAYER);
                                    }

                                    world.spawnEntity(saplingEntity);

                                    source.sendFeedback(() -> Text.literal("Spawned sapling at " + pos + " with origin " + originStr), false);
                                    return 1;
                                })
                        )
                )
        );
    }
}
