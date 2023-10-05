package com.minelittlepony.sockies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.sockies.compat.trinkets.Trinkets;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Sockies implements ModInitializer {
    public static final String DEFAULT_NAMESPACE = "sockies";
    public static final Logger LOGGER = LogManager.getLogger();

    public static Identifier id(String name) {
        return new Identifier(DEFAULT_NAMESPACE, name);
    }

    @Override
    public void onInitialize() {
        SItems.bootstrap();
        Trinkets.bootstrap();
    }
}
