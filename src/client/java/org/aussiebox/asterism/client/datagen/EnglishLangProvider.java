package org.aussiebox.asterism.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class EnglishLangProvider extends FabricLanguageProvider {
    public EnglishLangProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataGenerator, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.@NonNull WrapperLookup registryLookup, @NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.asterism", "Asterism");
        translationBuilder.add("item.asterism.astral_wyrmtooth", "Astral Wyrmtooth");
        translationBuilder.add("item.asterism.astral_wyrmtooth.tooltip", "Jade's weapon of choice");
    }
}
