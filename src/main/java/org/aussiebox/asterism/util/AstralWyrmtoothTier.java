package org.aussiebox.asterism.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;
import java.util.function.Function;

public class AstralWyrmtoothTier {
    public final int soulRequirement;
    public final Function<PlayerEntity, Boolean> enable;
    public final Consumer<PlayerEntity> disable;
    public final boolean removeOnHigherTiers;
    public final boolean runAlways;

    public AstralWyrmtoothTier(int soulRequirement, Function<PlayerEntity, Boolean> enable, Consumer<PlayerEntity> disable, boolean removeOnHigherTiers, boolean runAlways) {
        this.soulRequirement = soulRequirement;
        this.enable = enable;
        this.disable = disable;
        this.removeOnHigherTiers = removeOnHigherTiers;
        this.runAlways = runAlways;
    }
}
