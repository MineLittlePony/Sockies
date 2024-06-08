package com.minelittlepony.sockies;

import com.minelittlepony.sockies.item.SockColorsComponent;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

interface SItemGroups {
    static void bootstrap() {
        Registry.register(Registries.ITEM_GROUP, Sockies.id("socks"), FabricItemGroup.builder().entries((context, entries) -> {
            SItems.ALL_SOCKS.forEach(sock -> {
                sock.getPattern().getColors().forEach(colors -> {
                    entries.add(SockColorsComponent.setColors(sock.getDefaultStack(), colors));
                });
            });
        }).icon(() -> SItems.RAINBOW_SOCKS.getDefaultStack()).displayName(Text.translatable("mod.sockies.name")).build());
    }
}
