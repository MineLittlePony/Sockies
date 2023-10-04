package com.minelittlepony.sockies.client;

import com.minelittlepony.sockies.SItems;
import com.minelittlepony.sockies.item.SocksItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class SockiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AccessoryFeatureRenderer.register(SocksFeature::new);

        ColorProviderRegistry.ITEM.register((stack, i) -> {
            return ((SocksItem)stack.getItem()).getColor(stack, i);
        }, SItems.ALL_SOCKS.toArray(SocksItem[]::new));
    }
}
