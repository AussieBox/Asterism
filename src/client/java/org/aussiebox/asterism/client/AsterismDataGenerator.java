package org.aussiebox.asterism.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.aussiebox.asterism.client.datagen.EnglishLangProvider;
import org.aussiebox.asterism.client.datagen.ItemTagProvider;

public class AsterismDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(EnglishLangProvider::new);
        pack.addProvider(ItemTagProvider::new);
    }
}
