package org.aussiebox.asterism.item.custom;

import de.nexusrealms.nebulon.api.network.NebulonNetworking;
import de.nexusrealms.nebulon.api.render.ColorRgba;
import de.nexusrealms.nebulon.api.render.EffectHandle;
import de.nexusrealms.nebulon.api.render.effect.MagicCircle;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.cca.player.PlayerComponent;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.aussiebox.asterism.util.AsterismUtil;

import java.util.Random;

public class AstralWyrmtoothItem extends Item {
    public AstralWyrmtoothItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.getOrDefault(ModDataComponentTypes.DECIMATION_UPGRADE, false)) {
            return AsterismUtil.createMovingGradient(
                    super.getName(stack).getString(),
                    0xA0D2BB, 0xCEC6FF, 0xEC8080
            );
        } else return AsterismUtil.createMovingGradient(
                super.getName(stack).getString(),
                0xA0D2BB, 0xCEC6FF
        );
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isAlive()) return;
        if (!(attacker.getEntityWorld() instanceof ServerWorld world)) return;
        if (!(attacker instanceof PlayerEntity player)) return;
        if (!FabricLoader.getInstance().isDevelopmentEnvironment() && !(target instanceof PlayerEntity)) return; // Count non-player kills in dev
        PlayerComponent component = PlayerComponent.KEY.get(player);
        component.addSouls(1);
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return false;
    }
}
