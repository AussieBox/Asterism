package org.aussiebox.asterism.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
    public static MutableText createMovingGradient(String text, int... colors) {
        MutableText result = Text.empty();
        int length = text.length();
        if (length == 0) return result;

        if (colors == null || colors.length == 0) return Text.literal(text);
        if (colors.length == 1) return Text.literal(text).styled(s -> s.withColor(colors[0]).withItalic(false));

        long time = System.currentTimeMillis();
        float phase = (time % 2000L) / 2000.0f;

        for (int i = 0; i < length; ++i) {
            float charOffset = (float) i / (float) length;

            float progress = (float) (Math.sin((phase + charOffset) * Math.PI * 2) * 0.5 + 0.5);

            int currentColor = getMultiColorValue(colors, progress);

            MutableText part = Text.literal(String.valueOf(text.charAt(i)))
                    .styled((style) -> style.withColor(currentColor).withItalic(false));
            result.append(part);
        }

        return result;
    }

    private static int getMultiColorValue(int[] colors, float progress) {
        progress = Math.clamp(progress, 0.0f, 1.0f);

        int segments = colors.length - 1;
        float scaledProgress = progress * segments;
        int index = (int) Math.floor(scaledProgress);

        if (index >= segments) {
            return colors[colors.length - 1];
        }

        float segmentProgress = scaledProgress - index;

        return interpolateColor(colors[index], colors[index + 1], segmentProgress);
    }

    private static int interpolateColor(int colorStart, int colorEnd, float progress) {
        int r1 = colorStart >> 16 & 255;
        int g1 = colorStart >> 8 & 255;
        int b1 = colorStart & 255;

        int r2 = colorEnd >> 16 & 255;
        int g2 = colorEnd >> 8 & 255;
        int b2 = colorEnd & 255;

        int r = (int) (r1 + (r2 - r1) * progress);
        int g = (int) (g1 + (g2 - g1) * progress);
        int b = (int) (b1 + (b2 - b1) * progress);

        return (r << 16) | (g << 8) | b;
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

    public static void removeEnchantment(PlayerEntity player, ItemStack stack, RegistryKey<Enchantment> enchantmentKey, int level) {
        RegistryEntry<Enchantment> enchantment = player.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue())
                .orElseThrow();
        if (!stack.getEnchantments().getEnchantments().contains(enchantment)) return;
        stack.apply(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT, component -> {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(component);
            builder.remove(enchant -> enchant.equals(enchantment) && component.getLevel(enchantment) == level);
            return builder.build();
        });
    }

    public static void executeForAllOfItem(PlayerEntity player, Item item, Consumer<ItemStack> execute) {
        player.getInventory().getMainStacks().stream()
                .filter(itemStack -> itemStack.isOf(item))
                .forEach(execute);
    }

    public static AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed, float attackDamageBonus) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }
}
