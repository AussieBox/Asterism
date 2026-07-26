package org.aussiebox.asterism.cca.player;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec2f;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.asterism.util.AstralWyrmtoothTier;
import org.aussiebox.circuit_core.util.ExclusiveItemHolder;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<PlayerComponent> KEY = ComponentRegistry.getOrCreate(Asterism.id("player_component"), PlayerComponent.class);
    private final PlayerEntity player;

    @Getter private List<Vec2f> starHudOffsets = new ArrayList<>();

    @Getter private int souls;
    @Getter private int syncedWyrmtoothTier;
    @Getter private int wyrmtoothRegenTicks;
    @Getter private int wyrmtoothAbsTicks;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void addSouls(int souls) {
        int prevSouls = this.souls;
        this.souls = Math.clamp(this.souls + souls, 0, AsterismConstants.SOUL_CAP);

        if (this.souls != prevSouls) KEY.sync(player);
    }

    public void setSouls(int souls) {
        int prevSouls = this.souls;
        this.souls = Math.clamp(souls, 0, AsterismConstants.SOUL_CAP);

        if (this.souls != prevSouls) KEY.sync(player);
    }

    public void setSyncedWyrmtoothTier(int tier) {
        int prevTier = this.syncedWyrmtoothTier;
        this.syncedWyrmtoothTier = Math.clamp(tier, 0, AsterismConstants.AstralWyrmtooth.tiers.size()-1);

        this.starHudOffsets.clear();
        AstralWyrmtoothTier active = AsterismConstants.AstralWyrmtooth.tiers.get(syncedWyrmtoothTier);
        if (AsterismConstants.AstralWyrmtooth.tiers.getLast() == active) return;
        AstralWyrmtoothTier next = AsterismConstants.AstralWyrmtooth.tiers.get(syncedWyrmtoothTier+1);
        int diff = Math.abs(next.soulRequirement()-active.soulRequirement());
        Random random = new Random();

        for (int i = 0; i < diff; i++) this.starHudOffsets.add(new Vec2f(random.nextInt(0, 25), i * 15 + random.nextInt(0, 10)));

        Asterism.LOGGER.info("{} {} {}", this.syncedWyrmtoothTier, prevTier, this.syncedWyrmtoothTier != prevTier);
        if (this.syncedWyrmtoothTier != prevTier) KEY.sync(player);
    }

    @Override
    public void serverTick() {
        if (player instanceof ExclusiveItemHolder holder && holder.circuitCore$itemAllowed(ModItems.ASTRAL_WYRMTOOTH.build())) {
            for (AstralWyrmtoothTier tier : AsterismConstants.AstralWyrmtooth.tiers) {
                if (souls >= AsterismConstants.SOUL_UPGRADE_REQUIREMENT && !tier.upgraded() && syncedWyrmtoothTier < 7) {
                    tier.disable().accept(player);
                    continue;
                }
                if (AsterismConstants.AstralWyrmtooth.tiers.getLast() == tier) {
                    if (souls < tier.soulRequirement()) {
                        tier.disable().accept(player);
                    } else if (syncedWyrmtoothTier != AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) || tier.runAlways()) {
                        if (tier.enable().apply(player)) setSyncedWyrmtoothTier(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier));
                    }
                } else {
                    AstralWyrmtoothTier next = AsterismConstants.AstralWyrmtooth.tiers.get(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) + 1);
                    if (souls < tier.soulRequirement() || (souls >= next.soulRequirement() && tier.removeOnHigherTiers())) {
                        tier.disable().accept(player);
                    } else if (syncedWyrmtoothTier != AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier) || tier.runAlways()) {
                        if (tier.enable().apply(player)) setSyncedWyrmtoothTier(AsterismConstants.AstralWyrmtooth.tiers.indexOf(tier));
                    }
                }
            }

            if (souls >= 15 && souls < 30 || souls >= 44) {
                wyrmtoothRegenTicks++;
                if (wyrmtoothRegenTicks >= 50) {
                    player.heal(1.0F);
                    wyrmtoothRegenTicks = 0;
                }
            }

            if (souls >= 44) {
                wyrmtoothAbsTicks++;
                if (wyrmtoothAbsTicks >= 300) {
                    if (player.getAbsorptionAmount() < 4.0F) player.setAbsorptionAmount(4.0F);
                    wyrmtoothAbsTicks = 0;
                }
            }
        }
    }

    @Override
    public void readData(ReadView tag) {
        setSouls(tag.getInt("souls", 0));
        setSyncedWyrmtoothTier(tag.getInt("syncedWormtoothTier", -1));
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("souls", souls);
        tag.putInt("syncedWormtoothTier", syncedWyrmtoothTier);
    }
}
