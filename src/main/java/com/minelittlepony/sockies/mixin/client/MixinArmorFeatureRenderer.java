package com.minelittlepony.sockies.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minelittlepony.sockies.client.AccessoryFeatureRenderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorFeatureRenderer.class)
abstract class MixinArmorFeatureRenderer<
        T extends LivingEntity,
        M extends BipedEntityModel<T>,
        A extends BipedEntityModel<T>>
            extends FeatureRenderer<T, M> {

    private AccessoryFeatureRenderer<T, M, A> accessories;

    MixinArmorFeatureRenderer() { super(null); }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(FeatureRendererContext<T, M> context, A inner, A outer, BakedModelManager bakery, CallbackInfo info) {
        accessories = new AccessoryFeatureRenderer<>(context, inner, outer);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack stack, VertexConsumerProvider renderContext, int lightUv, T entity, float limbDistance, float limbAngle, float tickDelta, float age, float headYaw, float headPitch, CallbackInfo info) {
        accessories.render(stack, renderContext, lightUv, entity, limbDistance, limbAngle, tickDelta, age, headYaw, headPitch);
    }
}