package org.aussiebox.asterism.item.custom;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.aussiebox.asterism.util.AsterismUtil;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

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
}
