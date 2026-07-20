package org.aussiebox.asterism.cca.player;

import lombok.Getter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.aussiebox.asterism.AsterismUsers;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.asterism.util.AstralWyrmtoothTier;
import org.aussiebox.circuit_core.CircuitCore;
import org.aussiebox.circuit_core.util.ExclusiveItemHolder;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.UUID;

public class PlayerComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<PlayerComponent> KEY = ComponentRegistry.getOrCreate(Asterism.id("player_component"), PlayerComponent.class);
    private final PlayerEntity player;

    @Getter private int souls;
    @Getter private int syncedWyrmtoothTier;
    @Getter private int wyrmtoothRegenTicks;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void addSouls(int souls) {
        this.souls = Math.clamp(this.souls + souls, 0, AsterismConstants.SOUL_CAP);
    }

    public void setSouls(int souls) {
        this.souls = Math.clamp(souls, 0, AsterismConstants.SOUL_CAP);
    }

    public void setSyncedWyrmtoothTier(int tier) {
        this.syncedWyrmtoothTier = Math.clamp(tier, 0, 100);
    }

    @Override
    public void serverTick() {
        if (player instanceof ExclusiveItemHolder holder && holder.circuitCore$itemAllowed(ModItems.ASTRAL_WYRMTOOTH.build())) {
            for (AstralWyrmtoothTier tier : AsterismConstants.AstralWyrmtooth.tiers) {
                if (AsterismConstants.AstralWyrmtooth.tiers.getLast() == tier) {
                    if (souls < tier.soulRequirement) tier.disable.accept(player);
                    else if (syncedWyrmtoothTier != AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) || tier.runAlways) {
                        if (tier.enable.apply(player))
                            setSyncedWyrmtoothTier(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier));
                    }
                } else {
                    AstralWyrmtoothTier next = AsterismConstants.AstralWyrmtooth.tiers.get(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) + 1);
                    if (souls < tier.soulRequirement || (souls >= next.soulRequirement && tier.removeOnHigherTiers))
                        tier.disable.accept(player);
                    else if (syncedWyrmtoothTier != AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) || tier.runAlways) {
                        if (tier.enable.apply(player))
                            setSyncedWyrmtoothTier(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier));
                    }
                }
            }

            if (souls >= 15) {
                wyrmtoothRegenTicks++;
                if (wyrmtoothRegenTicks >= 50) {
                    player.heal(1.0F);
                    wyrmtoothRegenTicks = 0;
                }
            }
        }
    }

    @Override
    public void readData(ReadView tag) {
        setSouls(tag.getInt("souls", 0));
        setSyncedWyrmtoothTier(tag.getInt("syncedWormtoothTier", 0));
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("souls", souls);
        tag.putInt("syncedWormtoothTier", syncedWyrmtoothTier);
    }
}
