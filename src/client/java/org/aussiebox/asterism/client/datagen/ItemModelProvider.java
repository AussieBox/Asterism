package org.aussiebox.asterism.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.property.select.ComponentSelectProperty;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.aussiebox.asterism.item.ModItems;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ItemModelProvider extends FabricModelProvider {
    public static final Model SCYTHE = item("scythe", TextureKey.LAYER0);

    private static Model item(String parent, TextureKey requiredTextureKeys) {
        return new Model(Optional.of(Asterism.id("item/parent/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    public ItemModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerator generator) {
        ItemModel.Unbaked astralWyrmtooth = ItemModels.basic(generator.registerSubModel(ModItems.ASTRAL_WYRMTOOTH.build(), "-base", Models.HANDHELD));
        ItemModel.Unbaked astralWyrmtoothHand = ItemModels.basic(generator.registerSubModel(ModItems.ASTRAL_WYRMTOOTH.build(), "-base_hand", SCYTHE));
        ItemModel.Unbaked interstellarDecimation = ItemModels.basic(generator.registerSubModel(ModItems.ASTRAL_WYRMTOOTH.build(), "-upgraded", Models.HANDHELD));
        ItemModel.Unbaked interstellarDecimationHand = ItemModels.basic(generator.registerSubModel(ModItems.ASTRAL_WYRMTOOTH.build(), "-upgraded_hand", SCYTHE));

        generator.output.accept(
                ModItems.ASTRAL_WYRMTOOTH.build(),
                ItemModels.select(
                        new ComponentSelectProperty<>(ModDataComponentTypes.DECIMATION_UPGRADE),
                        ItemModelGenerator.createModelWithInHandVariant(astralWyrmtooth, astralWyrmtoothHand),
                        ItemModels.switchCase(true, ItemModelGenerator.createModelWithInHandVariant(interstellarDecimation, interstellarDecimationHand))
                )
        );
    }
}
