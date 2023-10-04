package com.minelittlepony.sockies;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface STags {
    TagKey<Item> SOCKS = item("socks");

    static TagKey<Item> item(String name) {
        return TagKey.of(RegistryKeys.ITEM, Sockies.id(name));
    }
}
