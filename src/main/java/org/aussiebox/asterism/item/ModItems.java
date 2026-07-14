package org.aussiebox.asterism.item;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.component.ModDataComponents;
import org.aussiebox.asterism.component.tooltip.TooltipLineComponent;
import org.aussiebox.asterism.item.custom.AstralWyrmtoothItem;
import org.aussiebox.circuit_core.helper.item.ItemBuilder;
import org.aussiebox.circuit_core.helper.item.ItemRegistry;

public class ModItems {
    public static final ItemBuilder<AstralWyrmtoothItem> ASTRAL_WYRMTOOTH = ItemRegistry.register(new ItemBuilder<>(
            Asterism.id("astral_wyrmtooth"),
            AstralWyrmtoothItem::new,
            new Item.Settings()
                    .component(ModDataComponents.TOOLTIP_LINE, new TooltipLineComponent(() -> Text.translatable("item.asterism.astral_wyrmtooth.tooltip"))),
            null
    ));

    public static void init() {

    }
}
