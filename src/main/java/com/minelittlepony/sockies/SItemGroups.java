package com.minelittlepony.sockies;

import com.minelittlepony.sockies.item.SockPattern;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

interface SItemGroups {
    static void bootstrap() {
        Registry.register(Registries.ITEM_GROUP, Sockies.id("socks"), FabricItemGroup.builder().entries((context, entries) -> {
            SItems.ALL_SOCKS.forEach(sock -> {
                sock.getPattern().getColors().forEach(colors -> {
                    ItemStack stack = sock.getDefaultStack();
                    for (int i = 0; i < colors.length; i++) {
                        sock.setColor(stack, i, colors[i]);
                    }
                    entries.add(stack);
                });
            });
        }).icon(() -> SItems.RAINBOW_SOCKS.setColors(SItems.RAINBOW_SOCKS.getDefaultStack(), SockPattern.RAINBOW_DEFAULT)).displayName(Text.translatable("mod.sockies.name")).build());
    }
}
