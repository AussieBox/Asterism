package org.aussiebox.asterism.item.custom;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.cca.player.PlayerComponent;
import org.aussiebox.asterism.util.AsterismUtil;

public class AstralWyrmtoothItem extends Item {
    public AstralWyrmtoothItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return AsterismUtil.createMovingGradient(
                super.getName(stack).getString(),
                0xA0D2BB, 0xCEC6FF
        );
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isAlive()) return;
        if (!(attacker instanceof PlayerEntity player)) return;
        if (!FabricLoader.getInstance().isDevelopmentEnvironment() && !(target instanceof PlayerEntity)) return; // Only continue with player kills outside of dev
        PlayerComponent component = PlayerComponent.KEY.get(player);
        component.addSouls(1);
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return false;
    }
}
