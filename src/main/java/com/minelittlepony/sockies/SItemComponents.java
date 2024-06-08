package com.minelittlepony.sockies;

import java.util.function.UnaryOperator;

import com.minelittlepony.sockies.item.SockColorsComponent;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface SItemComponents {
    ComponentType<SockColorsComponent> SOCK_COLORS = register("sock_colors", builder -> builder.codec(SockColorsComponent.CODEC).packetCodec(SockColorsComponent.PACKET_CODEC));

    static void bootstrap() { }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Sockies.id(id), builderOperator.apply(ComponentType.builder()).build());
    }
}
