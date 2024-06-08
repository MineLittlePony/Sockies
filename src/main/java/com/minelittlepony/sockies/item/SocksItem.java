package com.minelittlepony.sockies.item;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import net.minecraft.block.DispenserBlock;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SocksItem extends Item implements Equipment {
    private final SockMaterial material;
    private final SockPattern pattern;

    private final Supplier<AttributeModifiersComponent> attributeModifiers;

    public SocksItem(SockMaterial material, SockPattern pattern, Settings settings) {
        super(settings.maxCount(1));
        this.material = material;
        this.pattern = pattern;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
        this.attributeModifiers = Suppliers.memoize(() -> {
            var mat = material.getMaterial();
            AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(ArmorItem.Type.LEGGINGS.getEquipmentSlot());
            Identifier id = Identifier.ofVanilla("armor." + ArmorItem.Type.LEGGINGS.getName());

            AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(id, mat.getProtection(ArmorItem.Type.LEGGINGS), EntityAttributeModifier.Operation.ADD_VALUE), slot)
                    .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(id, mat.toughness(), EntityAttributeModifier.Operation.ADD_VALUE), slot);
            float knockbackResistance = mat.knockbackResistance();
            if (knockbackResistance > 0) {
                builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(id, knockbackResistance, EntityAttributeModifier.Operation.ADD_VALUE), slot);
            }
            return builder.build();
        });
    }

    public SockMaterial getMaterial() {
        return material;
    }

    @Override
    public int getEnchantability() {
        return material.getMaterial().enchantability();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.FEET;
    }

    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return material.getMaterial().equipSound();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return material.getMaterial().repairIngredient().get().test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return equipAndSwap(this, world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.sockies.pattern." + getPattern().name()));
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return attributeModifiers.get();
    }

    public SockPattern getPattern() {
        return pattern;
    }
}
