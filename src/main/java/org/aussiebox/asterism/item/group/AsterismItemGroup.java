package org.aussiebox.asterism.item.group;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.circuit_core.helper.itemgroup.ItemGroupSupplier;

public class AsterismItemGroup implements ItemGroupSupplier {
    @Override
    public Identifier getGroupId() {
        return Asterism.id(Asterism.MOD_ID);
    }

    @Override
    public ItemGroup.Builder getGroupBuilder() {
        return FabricItemGroup.builder().displayName(Text.translatable("itemGroup.asterism")).icon(ModItems.ASTRAL_WYRMTOOTH.build()::getDefaultStack);
    }
}
