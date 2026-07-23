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
        translationBuilder.add("item.asterism.astral_wyrmtooth.upgraded", "⯪☽ Interstellar Decimation ☯★");
        translationBuilder.add("item.asterism.astral_wyrmtooth.tooltip", "Jade's weapon of choice");

        translationBuilder.add("death.attack.astral_wyrmtooth", "%1$s was sliced to pieces");
        translationBuilder.add("death.attack.astral_wyrmtooth.player", "%1$s was sliced to pieces by %2$s");
        translationBuilder.add("death.attack.astral_wyrmtooth.item", "%1$s was sliced to pieces by %2$s using %3$s");

        translationBuilder.add("command.asterism.main.set_souls.error", "Failed to set Souls for player");
        translationBuilder.add("command.asterism.main.set_souls.success", "Set Souls of %1$s to %2$s");
    }
}
