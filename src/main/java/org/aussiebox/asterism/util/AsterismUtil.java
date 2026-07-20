package org.aussiebox.asterism.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class AsterismUtil {
    public static MutableText createMovingGradient(String text, int colorStart, int colorEnd) {
        MutableText result = Text.empty();
        int length = text.length();

        long time = System.currentTimeMillis();
        float phase = (time % 2000L) / 2000.0f;

        for (int i = 0; i < length; ++i) {
            float charOffset = (float) i / (float) Math.max(1, length);

            float progress = (float) (Math.sin((phase + charOffset) * Math.PI * 2) * 0.5 + 0.5);

            int currentColor = interpolateColor(colorStart, colorEnd, progress);
            MutableText part = Text.literal(String.valueOf(text.charAt(i)))
                    .styled((style) -> style.withColor(currentColor).withItalic(false));
            result.append(part);
        }

        return result;
    }

    private static int interpolateColor(int colorStart, int colorEnd, float progress) {
        int r1 = colorStart >> 16 & 255;
        int g1 = colorStart >> 8 & 255;
        int b1 = colorStart & 255;
        int r2 = colorEnd >> 16 & 255;
        int g2 = colorEnd >> 8 & 255;
        int b2 = colorEnd & 255;
        int r = (int)((float)r1 + (float)(r2 - r1) * progress);
        int g = (int)((float)g1 + (float)(g2 - g1) * progress);
        int b = (int)((float)b1 + (float)(b2 - b1) * progress);
        return (r << 16) + (g << 8) + b;
    }

    public static void addEnchantment(PlayerEntity player, ItemStack stack, RegistryKey<Enchantment> enchantmentKey, int level) {
        RegistryEntry<Enchantment> enchantment = player.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue())
                .orElseThrow();
        if (stack.getEnchantments().getEnchantments().contains(enchantment)) return;
        stack.addEnchantment(enchantment, level);
    }

    public static void removeEnchantment(PlayerEntity player, ItemStack stack, RegistryKey<Enchantment> enchantmentKey) {
        RegistryEntry<Enchantment> enchantment = player.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue())
                .orElseThrow();
        if (!stack.getEnchantments().getEnchantments().contains(enchantment)) return;
        stack.apply(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT, component -> {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(component);
            builder.remove(enchant -> enchant.equals(enchantment));
            return builder.build();
        });
    }

    public static void executeForAllOfItem(PlayerEntity player, Item item, Consumer<ItemStack> execute) {
        player.getInventory().getMainStacks().stream()
                .filter(itemStack -> itemStack.isOf(item))
                .forEach(execute);
    }
}
