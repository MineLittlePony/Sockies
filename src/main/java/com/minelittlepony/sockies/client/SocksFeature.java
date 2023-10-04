package com.minelittlepony.sockies.client;

import com.minelittlepony.sockies.STags;
import com.minelittlepony.sockies.item.SockPattern;
import com.minelittlepony.sockies.item.SocksItem;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SocksFeature<T extends LivingEntity, A extends BipedEntityModel<T>> implements AccessoryFeatureRenderer.Feature<T, A> {
    private final FeatureRendererContext<T, ? extends BipedEntityModel<T>> context;
    private final A model;

    public SocksFeature(FeatureRendererContext<T, ? extends BipedEntityModel<T>> context, A inner, A outer) {
        this.context = context;
        this.model = inner;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack socks = entity.getEquippedStack(EquipmentSlot.FEET);

        if (socks.getItem() instanceof SocksItem socksItem && socks.isIn(STags.SOCKS)) {
            context.getModel().copyBipedStateTo(model);
            model.setVisible(false);
            model.leftLeg.visible = true;
            model.rightLeg.visible = true;
            renderSocks(socks, model, matrices, vertexConsumers, light);
        }
    }

    public static void renderSocks(ItemStack socks, BipedEntityModel<?> model, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (socks.getItem() instanceof SocksItem socksItem && socks.isIn(STags.SOCKS)) {
            SockPattern pattern = socksItem.getPattern();

            for (int i = 0; i < pattern.layers(); i++) {
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(getTexture(socksItem, pattern, i)));
                int color = socksItem.getColor(socks, i);
                model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV,
                        ColorHelper.Argb.getRed(color) / 255F,
                        ColorHelper.Argb.getGreen(color) / 255F,
                        ColorHelper.Argb.getBlue(color) / 255F,
                        1
                );
            }
        }
    }

    public static Identifier getTexture(SocksItem item, SockPattern pattern, int layer) {
        return item.getMaterial().getId().withPath(p -> String.format("textures/models/armor/%s_%s_layer_%d.png", p, pattern.name(), layer + 1));
    }
}
