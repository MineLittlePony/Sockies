package com.minelittlepony.sockies.compat.minelittlepony;

import com.minelittlepony.api.model.gear.Gear;

import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Gear.register(SocksGear::new);
    }
}
