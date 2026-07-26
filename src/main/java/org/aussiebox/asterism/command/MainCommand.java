package org.aussiebox.asterism.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.aussiebox.asterism.cca.player.PlayerComponent;

public class MainCommand {
    public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(DefaultPermissions.GAMEMASTERS);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal(Asterism.MOD_ID)
                        .requires(CommandManager.requirePermissionLevel(PERMISSION_CHECK))
                        .then(CommandManager.literal("souls")
                                .then(CommandManager.literal("set")
                                        .then(CommandManager.argument("souls", IntegerArgumentType.integer(0, AsterismConstants.SOUL_CAP))
                                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                                        .executes(MainCommand::setSouls)
                                                )
                                        )
                                )
                        )
        );
    }

    public static int setSouls(CommandContext<ServerCommandSource> context) {
        try {
            PlayerEntity player = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
            int souls = context.getArgument("souls", Integer.class);
            PlayerComponent.KEY.get(player).setSouls(souls);
            context.getSource().sendMessage(Text.translatable("command.asterism.main.set_souls.success", player.getName(), souls));
        } catch (Exception e) {
            Asterism.LOGGER.error("Failed to set souls for player: {}", e.toString());
            context.getSource().sendError(Text.translatable("command.asterism.main.set_souls.error"));
        }

        return 1;
    }
}
