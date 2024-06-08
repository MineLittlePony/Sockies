package com.minelittlepony.sockies.item;

import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;

import com.minelittlepony.sockies.SItemComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SockColorsComponent(int[] colors, boolean showInTooltip) {
    private static final int[] DEFAULT_COLORS = new int[0];
    public static final Codec<SockColorsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT_STREAM.xmap(IntStream::toArray, IntStream::of).fieldOf("colors").forGetter(SockColorsComponent::colors),
        Codec.BOOL.fieldOf("show_in_tooltip").forGetter(SockColorsComponent::showInTooltip)
    ).apply(instance, SockColorsComponent::new));
    public static final PacketCodec<ByteBuf, SockColorsComponent> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER.collect(PacketCodecs.toCollection(IntArrayList::new)).xmap(l -> l.toIntArray(), IntArrayList::new), SockColorsComponent::colors,
        PacketCodecs.BOOL, SockColorsComponent::showInTooltip,
        SockColorsComponent::new
    );

    public static int[] getColors(ItemStack stack) {
        @Nullable
        SockColorsComponent component = stack.get(SItemComponents.SOCK_COLORS);
        return component == null ? DEFAULT_COLORS : component.colors();
    }

    public static int getColor(ItemStack stack, int index, int fallback) {
        @Nullable
        SockColorsComponent component = stack.get(SItemComponents.SOCK_COLORS);
        if (component == null || component.colors().length <= index) {
            return fallback;
        }
        return component.colors()[index];
    }

    public static ItemStack setColors(ItemStack stack, int[] colors) {
        stack.set(SItemComponents.SOCK_COLORS, new SockColorsComponent(colors, false));
        return stack;
    }
}
