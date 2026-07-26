package org.aussiebox.asterism;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.asterism.util.AsterismUtil;
import org.aussiebox.asterism.util.AstralWyrmtoothTier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface AsterismConstants {
    int SOUL_CAP = 44;
    int SOUL_UPGRADE_REQUIREMENT = 30;
    TagKey<Item> HELLFIRE_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Asterism.id("hellfire_tool_materials"));

    interface Textures {
        interface Hud {
            Identifier WHITE = Asterism.id("textures/white.png");
            interface AstralWyrmtooth {
                Identifier STAR_BIG = Asterism.id("textures/gui/hud/astral_wyrmtooth/star_big.png");
                Identifier STAR_SMALL = Asterism.id("textures/gui/hud/astral_wyrmtooth/star_small.png");
            }
        }
    }

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

            if (health != null && attackSpeed != null && movementSpeed != null && !health.hasModifier(HEALTH) && !attackSpeed.hasModifier(MINING_FATIGUE) && !movementSpeed.hasModifier(SPEED)) {
                health.addPersistentModifier(healthMod);
                attackSpeed.addPersistentModifier(attackSpeedMod);
                movementSpeed.addPersistentModifier(movementSpeedMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeInstance attackSpeed = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_SPEED);
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (health != null) {
                EntityAttributeModifier mod = health.getModifier(HEALTH);
                if (mod != null && mod.value() == -4) health.removeModifier(HEALTH);
            }
            if (attackSpeed != null) {
                EntityAttributeModifier mod = attackSpeed.getModifier(MINING_FATIGUE);
                if (mod != null && mod.value() == -0.1F) attackSpeed.removeModifier(MINING_FATIGUE);
            }
            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == -0.15F) movementSpeed.removeModifier(SPEED);
            }
        }, true, false, false);

        AstralWyrmtoothTier TIER_1 = new AstralWyrmtoothTier(1, player -> true, player -> {}, false, false, false);

        AstralWyrmtoothTier TIER_2 = new AstralWyrmtoothTier(3, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.addEnchantment(player, stack, Enchantments.SHARPNESS, 1));
            return true;
        }, player -> AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.removeEnchantment(player, stack, Enchantments.SHARPNESS)), false, true, false);

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
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == 0.2F) movementSpeed.removeModifier(SPEED);
            }
        }, false, false, false);

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
        }, false, false, false);

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
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == 0.4F) movementSpeed.removeModifier(SPEED);
            }
        }, false, false, false);

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
                EntityAttributeModifier mod = health.getModifier(HEALTH);
                if (mod != null && mod.value() == 4) health.removeModifier(HEALTH);
            }
        }, false, false, false);

        AstralWyrmtoothTier TIER_7 = new AstralWyrmtoothTier(30, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> {
                stack.set(ModDataComponentTypes.DECIMATION_UPGRADE, true);
                stack.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.asterism.astral_wyrmtooth.upgraded"));
                stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AsterismUtil.createSwordAttributeModifiers(5.0F, -3.1F, ModItems.HELLFIRE.attackDamageBonus()));
            });
            return true;
        }, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> {
                stack.set(ModDataComponentTypes.DECIMATION_UPGRADE, false);
                stack.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.asterism.astral_wyrmtooth"));
                stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AsterismUtil.createSwordAttributeModifiers(3.5F, -2.4F, ModItems.HELLFIRE.attackDamageBonus()));
            });
        }, false, true, true);

        AstralWyrmtoothTier TIER_7_TRUE = new AstralWyrmtoothTier(30, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeInstance attackSpeed = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_SPEED);
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            EntityAttributeModifier healthMod = new EntityAttributeModifier(HEALTH, -8, EntityAttributeModifier.Operation.ADD_VALUE);
            EntityAttributeModifier attackSpeedMod = new EntityAttributeModifier(MINING_FATIGUE, -0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL); // Imitates Mining Fatigue
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, -0.30F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL); // Imitates Slowness II

            if (health != null && attackSpeed != null && movementSpeed != null && !health.hasModifier(HEALTH) && !attackSpeed.hasModifier(MINING_FATIGUE) && !movementSpeed.hasModifier(SPEED)) {
                health.addPersistentModifier(healthMod);
                attackSpeed.addPersistentModifier(attackSpeedMod);
                movementSpeed.addPersistentModifier(movementSpeedMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
            EntityAttributeInstance attackSpeed = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_SPEED);
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (health != null) {
                EntityAttributeModifier mod = health.getModifier(HEALTH);
                if (mod != null && mod.value() == -8) health.removeModifier(HEALTH);
            }
            if (attackSpeed != null) {
                EntityAttributeModifier mod = attackSpeed.getModifier(MINING_FATIGUE);
                if (mod != null && mod.value() == -0.1F) attackSpeed.removeModifier(MINING_FATIGUE);
            }
            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == -0.30F) movementSpeed.removeModifier(SPEED);
            }
        }, false, false, true);

        /// Ignore Tier 8 as True Tier 7 fills its slot (and it just removes Tier 7's effects anyway)

        AstralWyrmtoothTier TIER_9 = new AstralWyrmtoothTier(32, player -> {
            AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.addEnchantment(player, stack, Enchantments.SHARPNESS, 2));
            return true;
        }, player -> AsterismUtil.executeForAllOfItem(player, ModItems.ASTRAL_WYRMTOOTH.build(), stack -> AsterismUtil.removeEnchantment(player, stack, Enchantments.SHARPNESS, 2)), false, true, false);

        AstralWyrmtoothTier TIER_10 = new AstralWyrmtoothTier(35, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, 0.4F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

            if (movementSpeed != null && !movementSpeed.hasModifier(SPEED)) {
                movementSpeed.addPersistentModifier(movementSpeedMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);

            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == 0.4F) movementSpeed.removeModifier(SPEED);
            }
        }, false, false, false);

        AstralWyrmtoothTier TIER_11 = new AstralWyrmtoothTier(38, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);
            EntityAttributeInstance strength = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_DAMAGE);
            EntityAttributeModifier movementSpeedMod = new EntityAttributeModifier(SPEED, 0.6F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            EntityAttributeModifier strengthMod = new EntityAttributeModifier(STRENGTH, 6.0F, EntityAttributeModifier.Operation.ADD_VALUE);

            if (movementSpeed != null && strength != null && !movementSpeed.hasModifier(SPEED) && !strength.hasModifier(STRENGTH)) {
                movementSpeed.addPersistentModifier(movementSpeedMod);
                strength.addPersistentModifier(strengthMod);
                return true;
            } else return false;
        }, player -> {
            EntityAttributeInstance movementSpeed = player.getAttributes().getCustomInstance(EntityAttributes.MOVEMENT_SPEED);
            EntityAttributeInstance strength = player.getAttributes().getCustomInstance(EntityAttributes.ATTACK_DAMAGE);
            
            if (movementSpeed != null) {
                EntityAttributeModifier mod = movementSpeed.getModifier(SPEED);
                if (mod != null && mod.value() == 0.6F) movementSpeed.removeModifier(SPEED);
            }
            if (strength != null) {
                EntityAttributeModifier mod = strength.getModifier(STRENGTH);
                if (mod != null && mod.value() == 6.0F) strength.removeModifier(STRENGTH);
            }
        }, false, false, false);

        /// Tier 12 handled in loop

        List<AstralWyrmtoothTier> tiers = new ArrayList<>(List.of(TIER_0, TIER_1, TIER_2, TIER_3, TIER_4, TIER_5, TIER_6, TIER_7, TIER_7_TRUE, TIER_9, TIER_10, TIER_11));
    }
}
