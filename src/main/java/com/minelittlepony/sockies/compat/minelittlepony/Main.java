package com.minelittlepony.sockies.compat.minelittlepony;

import com.minelittlepony.api.model.gear.IGear;

import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IGear.register(SocksGear::new);
    }
}
