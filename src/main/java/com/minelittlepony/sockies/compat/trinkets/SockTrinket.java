package com.minelittlepony.sockies.compat.trinkets;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.minelittlepony.sockies.SItems;
import com.minelittlepony.sockies.item.SocksItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Identifier;

class SockTrinket implements Trinket {
    private static final Identifier SHOES_SLOT = new Identifier("feet:shoes");

    public static void bootstrap() {
        SItems.ALL_SOCKS.forEach(sock -> {
            TrinketsApi.registerTrinket(sock, new SockTrinket(sock));
        });
    }

    public static Stream<ItemStack> getSocks(LivingEntity entity) {
        return getInventory(entity, SHOES_SLOT).stream().flatMap(SockTrinket::stream);
    }

    public static Optional<TrinketInventory> getInventory(LivingEntity entity, Identifier slot) {
        return TrinketsApi.getTrinketComponent(entity)
                .map(component -> component.getInventory()
                .getOrDefault(slot.getNamespace(), Map.of())
                .getOrDefault(slot.getPath(), null)
        );
    }

    static Stream<ItemStack> stream(Inventory inventory) {
        return slots(inventory).map(inventory::getStack);
    }

    static Stream<Integer> slots(Inventory inventory) {
        return Stream.iterate(0, i -> i < inventory.size(), i -> i + 1);
    }

    private final SocksItem item;

    SockTrinket(SocksItem item) {
        this.item = item;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.isSpectator() || stack.isEmpty()) {
            return;
        }

        if (stack.getItem() instanceof Equipment q) {
            entity.playSound(q.getEquipSound(), 1, 1);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return slot.inventory().getStack(slot.index()).isEmpty();
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return !(EnchantmentHelper.hasBindingCurse(stack) && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = Trinket.super.getModifiers(stack, slot, entity, uuid);
        item.getAttributeModifiers(item.getSlotType());
        return modifiers;
    }
}