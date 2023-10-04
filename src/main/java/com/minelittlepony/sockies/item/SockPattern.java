package com.minelittlepony.sockies.item;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.util.DyeColor;

public record SockPattern(String name, int layers, Supplier<Iterable<int[]>> colors) {
    private static final int WHITE = 0xFFFFFFFF;
    public static final int[] RAINBOW_DEFAULT = { 0x667f33, 0xffffff, 0x7f3fb2, 0xd37f33 };
    private static final int[] COLORS = Arrays.stream(DyeColor.values())
            .filter(color -> color != DyeColor.BLACK && color != DyeColor.WHITE)
            .mapToInt(DyeColor::getFireworkColor).toArray();

    public static final SockPattern PLAIN = new SockPattern("plain", 1, () -> {
        return Stream.concat(Stream.of(new int[] { WHITE }), Arrays.stream(COLORS).mapToObj(i -> new int[] { i })).toList();
    });
    public static final SockPattern DOTS = new SockPattern("dots", 2, () -> {
        return IntStream.range(0, COLORS.length / 2).mapToObj(i -> {
           return new int[] { COLORS[i * 2], COLORS[(i * 2) + 1] };
        }).toList();
    });
    public static final SockPattern STRIPES = new SockPattern("striped", 3, () -> {
        return IntStream.range(0, COLORS.length / 3).mapToObj(i -> {
            return new int[] { COLORS[i * 3], COLORS[(i * 3) + 1], COLORS[(i * 3) + 2]  };
         }).toList();
    });
    public static final SockPattern RAINBOWS = new SockPattern("rainbows", 4, () -> {
        return Stream.concat(Stream.of(RAINBOW_DEFAULT), IntStream.range(0, COLORS.length / 4).mapToObj(i -> {
            return new int[] { COLORS[i * 4], COLORS[(i * 4) + 1], COLORS[(i * 4) + 2], COLORS[(i * 4) + 3]  };
        })).toList();
    });

    public Iterable<int[]> getColors() {
        return colors.get();
    }
}
