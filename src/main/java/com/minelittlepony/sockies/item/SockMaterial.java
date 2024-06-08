package com.minelittlepony.sockies.item;

import java.util.List;
import java.util.Map;
import com.minelittlepony.sockies.Sockies;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class SockMaterial  {
    private static final Ingredient REPAIR_INGREDIENT = Ingredient.fromTag(ItemTags.WOOL);

    public static final SockMaterial WOOL = new SockMaterial(Sockies.id("wool"), new ArmorMaterial(
            Map.of(),
            0,
            RegistryEntry.of(SoundEvents.BLOCK_WOOL_PLACE),
            () -> REPAIR_INGREDIENT,
            List.of(),
            0,
            0
    ));

    private final Identifier id;
    private final ArmorMaterial material;

    public SockMaterial(Identifier id, ArmorMaterial material) {
        this.id = id;
        this.material = material;
    }

    public ArmorMaterial getMaterial() {
        return material;
    }

    public Identifier getId() {
        return id;
    }
}
