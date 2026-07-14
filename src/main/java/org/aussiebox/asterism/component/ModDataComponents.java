package org.aussiebox.asterism.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.component.tooltip.TooltipLineComponent;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final ComponentType<TooltipLineComponent> TOOLTIP_LINE = register(
            "tooltip_line",
            builder -> builder.codec(TooltipLineComponent.CODEC)
    );

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Asterism.id(name),
                builderOperator.apply(ComponentType.builder()).build());
    }
}
