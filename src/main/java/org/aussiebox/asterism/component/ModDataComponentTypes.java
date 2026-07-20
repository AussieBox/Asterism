package org.aussiebox.asterism.component;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.component.tooltip.TooltipLineComponent;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<TooltipLineComponent> TOOLTIP_LINE = register(
            "tooltip_line",
            builder -> builder.codec(TooltipLineComponent.CODEC)
    );

    public static final ComponentType<Boolean> DECIMATION_UPGRADE = register(
            "interstellar_decimation_upgrade",
            builder -> builder.codec(Codec.BOOL)
    );


    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Asterism.id(name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {
        ComponentTooltipAppenderRegistry.addAfter(DataComponentTypes.TRIM, TOOLTIP_LINE);
    }
}
