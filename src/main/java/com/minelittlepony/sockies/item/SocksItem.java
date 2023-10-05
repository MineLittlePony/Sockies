package com.minelittlepony.sockies.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SocksItem extends Item implements DyeableItem, Equipment {
    private final SockMaterial material;
    private final SockPattern pattern;

    public SocksItem(SockMaterial material, SockPattern pattern, Settings settings) {
        super(settings.maxCount(1));
        this.material = material;
        this.pattern = pattern;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public SockMaterial getMaterial() {
        return material;
    }

    @Override
    public int getEnchantability() {
        return material.getEnchantability();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.FEET;
    }

    @Override
    public SoundEvent getEquipSound() {
        return material.getEquipSound();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return equipAndSwap(this, world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.sockies.pattern." + getPattern().name()));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == getSlotType() ? material.getAttributeModifiers() : super.getAttributeModifiers(slot);
    }

    static String getDisplayKey(int layer) {
        return layer == 0 ? DISPLAY_KEY : DISPLAY_KEY + "_" + layer;
    }

    public boolean hasColor(ItemStack stack, int layer) {
        NbtCompound nbtCompound = stack.getSubNbt(getDisplayKey(layer));
        return nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE);
    }

    public ItemStack setColors(ItemStack stack, int[] colors) {
        for (int i = 0; i < colors.length && i < getPattern().layers(); i++) {
            setColor(stack, i, colors[i]);
        }
        return stack;
    }

    public void setColor(ItemStack stack, int layer, int color) {
        stack.getOrCreateSubNbt(getDisplayKey(layer)).putInt(COLOR_KEY, color);
    }

    public int getColor(ItemStack stack, int layer) {
        NbtCompound nbtCompound = stack.getSubNbt(getDisplayKey(layer));
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return 0xFFFFFF;
    }

    @Override
    public void removeColor(ItemStack stack) {
        for (int i = 0; i < getPattern().layers(); i++) {
            removeColor(stack, i);
        }
    }

    public void removeColor(ItemStack stack, int layer) {
        NbtCompound nbtCompound = stack.getSubNbt(getDisplayKey(layer));
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY)) {
            nbtCompound.remove(COLOR_KEY);
        }
    }

    public SockPattern getPattern() {
        return pattern;
    }
}
