package net.ohacd.poh.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.ohacd.poh.component.FatigueComponent;
import net.ohacd.poh.component.ModComponents;

public class FatigueCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("fatigue")
                .requires(source -> source.hasPermissionLevel(2)) // OP only

                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(ctx -> {
                                    PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                    FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
                                    ctx.getSource().sendFeedback(() -> Text.literal(player.getName().getString() + " fatigue: " + fatigue.getFatigue()), false);
                                    return 1;
                                })))

                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("value", FloatArgumentType.floatArg(0.0f, 1.0f))
                                        .executes(ctx -> {
                                            PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                            float value = FloatArgumentType.getFloat(ctx, "value");
                                            FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
                                            fatigue.setFatigue(value);
                                            ctx.getSource().sendFeedback(() -> Text.literal("Set " + player.getName().getString() + "'s fatigue to " + value), true);
                                            return 1;
                                        }))))

                .then(CommandManager.literal("show")
                        .executes(ctx -> {
                            PlayerEntity player = ctx.getSource().getPlayer();
                            FatigueComponent fatigue = ModComponents.FATIGUE.get(player);
                            ctx.getSource().sendFeedback(() -> Text.literal("Your fatigue: " + fatigue.getFatigue()), false);
                            return 1;
                        }))
        );
    }
}
