package org.aussiebox.asterism.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;
import java.util.function.Function;

public record AstralWyrmtoothTier(int soulRequirement, Function<PlayerEntity, Boolean> enable,
                                  Consumer<PlayerEntity> disable, boolean removeOnHigherTiers, boolean runAlways,
                                  boolean upgraded) {
}
