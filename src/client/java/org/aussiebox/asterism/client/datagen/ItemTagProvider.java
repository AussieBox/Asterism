package org.aussiebox.asterism.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.@NonNull WrapperLookup registries) {
        this.getTagBuilder(AsterismConstants.HELLFIRE_TOOL_MATERIALS)
                .add(Asterism.id("hellsteel_ingot"))
                .build();
    }
}
