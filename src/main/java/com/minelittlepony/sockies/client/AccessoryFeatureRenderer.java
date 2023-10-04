package com.minelittlepony.sockies.client;

import java.util.*;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class AccessoryFeatureRenderer<
        T extends LivingEntity,
        M extends BipedEntityModel<T>,
        A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    private static final List<FeatureFactory<?, ?>> REGISTRY = new ArrayList<>();

    public static <T extends LivingEntity, A extends BipedEntityModel<T>> void register(FeatureFactory<T, A> factory) {
        REGISTRY.add(factory);
    }

    private final Iterable<Feature<T, A>> features;

    @SuppressWarnings("unchecked")
    public AccessoryFeatureRenderer(FeatureRendererContext<T, M> context, A inner, A outer) {
        super(context);
        features = REGISTRY.stream().map(f -> ((FeatureFactory<T, A>)f).create(context, inner, outer)).toList();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        features.forEach(feature -> feature.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch));
    }

    public interface FeatureFactory<T extends LivingEntity, A extends BipedEntityModel<T>> {
        Feature<T, A> create(FeatureRendererContext<T, ? extends BipedEntityModel<T>> context, A inner, A outer);
    }

    public interface Feature<T extends LivingEntity, A extends BipedEntityModel<T>> {
        void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch);
    }
}
