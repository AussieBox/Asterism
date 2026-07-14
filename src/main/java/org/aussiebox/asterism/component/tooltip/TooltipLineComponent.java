package org.aussiebox.asterism.component.tooltip;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record TooltipLineComponent(Supplier<Text> text) implements TooltipAppender {
    public static final Codec<Supplier<Text>> TEXT_SUPPLIER_CODEC = TextCodecs.CODEC.xmap(
            text -> () -> text,
            Supplier::get
    );

    public static final Codec<TooltipLineComponent> CODEC = TEXT_SUPPLIER_CODEC.xmap(TooltipLineComponent::new, TooltipLineComponent::text);

    public TooltipLineComponent(Supplier<Text> text) {
        this.text = text;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> queue, TooltipType type, ComponentsAccess components) {
        queue.accept(text.get());
    }
}
