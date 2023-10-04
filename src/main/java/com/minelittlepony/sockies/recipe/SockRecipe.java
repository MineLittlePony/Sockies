package com.minelittlepony.sockies.recipe;

import com.google.gson.JsonObject;
import com.minelittlepony.sockies.item.SocksItem;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SockRecipe extends ShapedRecipe {
    SockRecipe(ShapedRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getCategory(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput(null));
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager manager) {
        ItemStack result = super.craft(inventory, manager);
        if (!(result.getItem() instanceof SocksItem sock)) {
            return result;
        }

        IntList colors = getColors(inventory);

        for (int i = 0; i < sock.getPattern().layers(); i++) {
            if (i < colors.size()) {
                sock.setColor(result, i, colors.getInt(i));
            }
        }

        return result;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        return super.matches(inventory, world)
                && (!(getOutput(null).getItem() instanceof SocksItem sock) || getColors(inventory).size() == sock.getPattern().layers());
    }

    private IntList getColors(RecipeInputInventory inventory) {
        IntList colors = new IntArrayList();

        inventory.getInputStacks().forEach(stack -> {
            if (stack.getItem() instanceof DyeableItem dyeable) {
                int color = dyeable.getColor(stack);
                if (!colors.contains(color)) {
                    colors.add(color);
                }
            } else if (stack.getItem() instanceof BlockItem block) {
                int color = block.getBlock().getDefaultMapColor().color;
                if (!colors.contains(color)) {
                    colors.add(color);
                }
            }
        });
        return colors;
    }

    public static class Serializer extends ShapedRecipe.Serializer {
        @Override
        public ShapedRecipe read(Identifier id, JsonObject json) {
            return new SockRecipe(super.read(id, json));
        }

        @Override
        public ShapedRecipe read(Identifier id, PacketByteBuf buffer) {
            return new SockRecipe(super.read(id, buffer));
        }
    }
}
