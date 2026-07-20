package org.aussiebox.asterism;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.asterism.util.AsterismUtil;
import org.aussiebox.asterism.util.AstralWyrmtoothTier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface AsterismConstants {
    int SOUL_CAP = 30;
    TagKey<Item> HELLFIRE_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Asterism.id("hellfire_tool_materials"));

    interface AstralWyrmtooth {
        Identifier HEALTH = Asterism.id("astral_wyrmtooth/health");
        Identifier MINING_FATIGUE = Asterism.id("astral_wyrmtooth/mining_fatigue");
        Identifier SPEED = Asterism.id("astral_wyrmtooth/speed");
        Identifier STRENGTH = Asterism.id("astral_wyrmtooth/strength");

        AstralWyrmtoothTier TIER_0 = new AstralWyrmtoothTier(0, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeInstance attackSpeed = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_SPEED);
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            EntityAttributeModifier healthMod = new EntityAttributeModifier(HEALTH, -4, EntityAttributeModifier.Operation.ADD_VALUE);
            EntityAttributeModifier attackSpeedMod = new EntityAttributeModifier(MINING_FATIGUE, -0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL); // Imitates Mining Fatigue
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, -0.15F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL); // Imitates Slowness

            if (health != null) health.addPersistentModifier(healthMod);
            if (attackSpeed != null) attackSpeed.addPersistentModifier(attackSpeedMod);
            if (movementSpeed != null) movementSpeed.addPersistentModifier(movementSpeedMod);

            return true;
        }, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeInstance attackSpeed = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_SPEED);
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (health != null) {
                EntityAttributeModifier mod = health.getModifier(AsterismConstants.AstralWyrmtooth.HEALTH);
                if (mod != null && mod.value() == -4) health.removeModifier(AsterismConstants.AstralWyrmtooth.HEALTH);
            }
            if (attackSpeed != null) {
                EntityAttributeModifier mod = attackSpeed.getModifier(AsterismConstants.AstralWyrmtooth.MINING_FATIGUE);
                if (mod != null && mod.value() == -0.1F) attackSpeed.removeModifier(AsterismConstants.AstralWyrmtooth.MINING_FATIGUE);
            }
            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(AsterismConstants.AstralWyrmtooth.SPEED);
                if (mod != null && mod.value() == -0.15F) movementSpeed.removeModifier(AsterismConstants.AstralWyrmtooth.SPEED);
            }
        }, true, false);

        AstralWyrmtoothTier TIER_1 = new AstralWyrmtoothTier(1, player -> true, player -> {}, false, false);

        AstralWyrmtoothTier TIER_2 = new AstralWyrmtoothTier(3, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.addEnchantment(player, stack, Enchantments.SHARPNESS, 1));
            return true;
        }, player -> AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.removeEnchantment(player, stack, Enchantments.SHARPNESS)), false, true);

        AstralWyrmtoothTier TIER_3 = new AstralWyrmtoothTier(5, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, 0.2F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

            if (movementSpeed != null && !movementSpeed.hasModifier(SPEED)) {
                movementSpeed.addPersistentModifier(movementSpeedMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(AsterismConstants.AstralWyrmtooth.SPEED);
                if (mod != null && mod.value() == 0.2F) movementSpeed.removeModifier(AsterismConstants.AstralWyrmtooth.SPEED);
            }
        }, false, false);

        AstralWyrmtoothTier TIER_4 = new AstralWyrmtoothTier(10, player -> {
            EntityAttributeInstance strength = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_DAMAGE);
            EntityAttributeModifier strengthMod = new EntityAttributeModifier(STRENGTH, 3.0F, EntityAttributeModifier.Operation.ADD_VALUE);

            if (strength != null && !strength.hasModifier(STRENGTH)) {
                strength.addPersistentModifier(strengthMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance strength = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_DAMAGE);

            if (strength != null) {
                EntityAttributeModifier mod = strength.getModifier(STRENGTH);
                if (mod != null && mod.value() == 3.0F) strength.removeModifier(STRENGTH);
            }
        }, false, false);

        /// Regen handled in {@link org.aussiebox.asterism.cca.player.PlayerComponent PlayerComponent}
        AstralWyrmtoothTier TIER_5 = new AstralWyrmtoothTier(15, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, 0.4F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

            if (movementSpeed != null && !movementSpeed.hasModifier(SPEED)) {
                movementSpeed.addPersistentModifier(movementSpeedMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(AsterismConstants.AstralWyrmtooth.SPEED);
                if (mod != null && mod.value() == 0.4F) movementSpeed.removeModifier(AsterismConstants.AstralWyrmtooth.SPEED);
            }
        }, false, false);

        AstralWyrmtoothTier TIER_6 = new AstralWyrmtoothTier(20, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeModifier healthMod = new EntityAttributeModifier(HEALTH, 4, EntityAttributeModifier.Operation.ADD_VALUE);

            if (health != null && !health.hasModifier(HEALTH)) {
                health.addPersistentModifier(healthMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);

            if (health != null) {
                EntityAttributeModifier mod = health.getModifier(AsterismConstants.AstralWyrmtooth.HEALTH);
                if (mod != null && mod.value() == 4) health.removeModifier(AsterismConstants.AstralWyrmtooth.HEALTH);
            }
        }, false, false);

        AstralWyrmtoothTier TIER_7 = new AstralWyrmtoothTier(30, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> stack.set(ModDataComponentTypes.DECIMATION_UPGRADE, true));
            return true;
        }, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> stack.set(ModDataComponentTypes.DECIMATION_UPGRADE, false));
        }, false, true);

        List<AstralWyrmtoothTier> tiers = new ArrayList<>(List.of(TIER_0, TIER_1, TIER_2, TIER_3, TIER_4, TIER_5, TIER_6, TIER_7));
    }
}
