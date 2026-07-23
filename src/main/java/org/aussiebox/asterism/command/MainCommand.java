package org.aussiebox.asterism.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//? 1.21.11
//import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.aussiebox.asterism.cca.player.PlayerComponent;
import org.aussiebox.circuit_core.CircuitCore;
import org.aussiebox.circuit_core.CircuitCoreConstants;
import org.aussiebox.circuit_core.network.SetAnimationS2CPayload;
import org.aussiebox.circuit_core.network.SetStackAnimationS2CPayload;
import org.aussiebox.circuit_core.pal.ControllerRegistry;
import org.aussiebox.circuit_core.pal.PALAnimation;
import org.aussiebox.circuit_core.pal.PALController;
import org.aussiebox.circuit_core.pal.PALHelper;
import org.aussiebox.circuit_core.pal.animation.AnimationData;
import org.aussiebox.circuit_core.pal.animation.StackAnimationData;
import org.aussiebox.circuit_core.util.Hand;

import java.util.Objects;
import java.util.Optional;

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
