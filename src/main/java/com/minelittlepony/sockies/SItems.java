package com.minelittlepony.sockies;

import java.util.ArrayList;
import java.util.List;

import com.minelittlepony.sockies.item.SockMaterial;
import com.minelittlepony.sockies.item.SockPattern;
import com.minelittlepony.sockies.item.SocksItem;
import com.minelittlepony.sockies.recipe.SRecipes;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface SItems {
    List<SocksItem> ALL_SOCKS = new ArrayList<>();
    SocksItem PLAIN_SOCKS = register("plain_socks", new SocksItem(SockMaterial.WOOL, SockPattern.PLAIN, new Item.Settings()));
    SocksItem STRIPED_SOCKS = register("striped_socks", new SocksItem(SockMaterial.WOOL, SockPattern.STRIPES, new Item.Settings()));
    SocksItem POLKADOT_SOCKS = register("polkadot_socks", new SocksItem(SockMaterial.WOOL, SockPattern.DOTS, new Item.Settings()));
    SocksItem RAINBOW_SOCKS = register("rainbow_socks", new SocksItem(SockMaterial.WOOL, SockPattern.RAINBOWS, new Item.Settings()));

    private static <T extends Item> T register(String name, T item) {
        if (item instanceof SocksItem socks) {
            ALL_SOCKS.add(socks);
        }
        return Registry.register(Registries.ITEM, Sockies.id(name), item);
    }

    static void bootstrap() {
        SItemGroups.bootstrap();
        SRecipes.bootstrap();
    }
}
