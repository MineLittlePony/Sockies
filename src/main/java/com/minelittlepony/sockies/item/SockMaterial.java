package com.minelittlepony.sockies.item;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.minelittlepony.sockies.Sockies;

import net.minecraft.item.ArmorItem.Type;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class SockMaterial implements ArmorMaterial {
    private static final Ingredient REPAIR_INGREDIENT = Ingredient.fromTag(ItemTags.WOOL);

    public static final SockMaterial WOOL = new SockMaterial(Sockies.id("wool"), ImmutableMultimap::of);

    private final Identifier id;

    private final Supplier<Multimap<EntityAttribute, EntityAttributeModifier>> modifiers;

    public SockMaterial(Identifier id, Supplier<Multimap<EntityAttribute, EntityAttributeModifier>> modifiers) {
        this.id = id;
        this.modifiers = Suppliers.memoize(modifiers::get)::get;
    }

    @Override
    public int getDurability(Type var1) {
        return 0;
    }

    @Override
    public int getProtection(Type var1) {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BLOCK_WOOL_PLACE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return REPAIR_INGREDIENT;
    }

    @Override
    public String getName() {
        return id.toString();
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers() {
        return modifiers.get();
    }
}
