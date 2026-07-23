package org.aussiebox.asterism.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismUsers;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.aussiebox.asterism.component.tooltip.TooltipLineComponent;
import org.aussiebox.asterism.item.custom.AstralWyrmtoothItem;
import org.aussiebox.circuit_core.helper.PlayerExclusiveItemHelper;
import org.aussiebox.circuit_core.helper.item.ItemBuilder;
import org.aussiebox.circuit_core.helper.item.ItemRegistry;
import org.aussiebox.circuit_core.helper.item.ItemSupplier;

import java.util.Collections;

public class ModItems implements ItemSupplier {
    public static final ToolMaterial HELLFIRE = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3554, 10.5F, 3.5F, 20, TagKey.of(RegistryKeys.ITEM, Asterism.id("astral_tool_materials")));

    public static final ItemBuilder<AstralWyrmtoothItem> ASTRAL_WYRMTOOTH = ItemRegistry.register(new ItemBuilder<>(
            Asterism.id("astral_wyrmtooth"),
            AstralWyrmtoothItem::new,
            new Item.Settings()
                    .component(ModDataComponentTypes.TOOLTIP_LINE, new TooltipLineComponent(() -> Collections.singletonList(Text.translatable("item.asterism.astral_wyrmtooth.tooltip").withColor(Colors.LIGHT_GRAY))))
                    .component(DataComponentTypes.DAMAGE_TYPE, new LazyRegistryEntryReference<>(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Asterism.id("astral_wyrmtooth"))))
                    .sword(HELLFIRE, 3.5F, -2.4F)
                    .component(DataComponentTypes.WEAPON, new WeaponComponent(1, 1.0F)),
            null
    ));

    static {
        PlayerExclusiveItemHelper.makeExclusive(ASTRAL_WYRMTOOTH.build(), AsterismUsers.JadeCrystalHeart);
    }
}
