package org.aussiebox.asterism;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface AsterismConstants {
    int SOUL_CAP = 30;
    TagKey<Item> HELLFIRE_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Asterism.id("hellfire_tool_materials"));
}
