package com.minelittlepony.sockies.compat.trinkets;

import java.util.stream.Stream;

import com.minelittlepony.sockies.STags;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface Trinkets {
    boolean LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

    static Stream<ItemStack> getSocks(LivingEntity entity) {
        Stream<ItemStack> stacks = Stream.of(entity.getEquippedStack(EquipmentSlot.FEET));
        if (!LOADED) {
            return stacks.filter(i -> i.isIn(STags.SOCKS));
        }
        return Stream.concat(SockTrinket.getSocks(entity), stacks).filter(i -> i.isIn(STags.SOCKS));
    }

    static void bootstrap() {
        if (LOADED) {
            SockTrinket.bootstrap();
        }
    }
}
