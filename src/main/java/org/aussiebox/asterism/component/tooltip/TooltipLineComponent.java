package org.aussiebox.asterism.component.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import javax.print.attribute.TextSyntax;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record TooltipLineComponent(Supplier<List<Text>> text) implements TooltipAppender {
    public static final Codec<Supplier<List<Text>>> TEXT_SUPPLIER_CODEC = TextCodecs.CODEC.listOf().xmap(
            text -> () -> text,
            Supplier::get
    );

    public static final Codec<TooltipLineComponent> CODEC = TEXT_SUPPLIER_CODEC.xmap(TooltipLineComponent::new, TooltipLineComponent::text);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> queue, TooltipType type, ComponentsAccess components) {
        text.get().forEach(queue);
    }
}
