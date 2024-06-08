package com.minelittlepony.sockies.recipe;

import com.minelittlepony.sockies.item.SockColorsComponent;
import com.minelittlepony.sockies.item.SocksItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;

public class SockRecipe extends ShapedRecipe {
    final ItemStack result;
    final RawShapedRecipe raw;
    public SockRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.result = result;
        this.raw = raw;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, WrapperLookup manager) {
        return SockColorsComponent.setColors(super.craft(inventory, manager), getColors(inventory).toIntArray());
    }

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        return super.matches(inventory, world)
                && (!(getResult(null).getItem() instanceof SocksItem sock) || getColors(inventory).size() == sock.getPattern().layers());
    }

    private IntList getColors(CraftingRecipeInput inventory) {
        IntList colors = new IntArrayList();

        inventory.getStacks().forEach(stack -> {
            var dye = stack.get(DataComponentTypes.DYED_COLOR);
            if (dye != null) {
                int color = dye.rgb();
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

    public static class Serializer implements RecipeSerializer<SockRecipe> {
        private static final MapCodec<SockRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(SockRecipe::getGroup),
            CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(SockRecipe::getCategory),
            RawShapedRecipe.CODEC.forGetter(recipe -> recipe.raw),
            ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(SockRecipe::showNotification)
        ).apply(instance, SockRecipe::new));
        private static final PacketCodec<RegistryByteBuf, SockRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                (buf, recipe) -> {
                    buf.writeString(recipe.getGroup());
                    buf.writeEnumConstant(recipe.getCategory());
                    RawShapedRecipe.PACKET_CODEC.encode(buf, recipe.raw);
                    ItemStack.PACKET_CODEC.encode(buf, recipe.getResult(null));
                    buf.writeBoolean(recipe.showNotification());
                }, (buf) -> new SockRecipe(
                    buf.readString(),
                    buf.readEnumConstant(CraftingRecipeCategory.class),
                    RawShapedRecipe.PACKET_CODEC.decode(buf),
                    ItemStack.PACKET_CODEC.decode(buf),
                    buf.readBoolean()
                )
        );

        @Override
        public MapCodec<SockRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SockRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
