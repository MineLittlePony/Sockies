package com.minelittlepony.sockies.compat.minelittlepony;

import java.util.UUID;

import com.minelittlepony.api.model.BodyPart;
import com.minelittlepony.api.model.PonyModel;
import com.minelittlepony.api.model.gear.Gear;
import com.minelittlepony.client.model.ModelType;
import com.minelittlepony.client.model.armour.ArmourLayer;
import com.minelittlepony.client.model.armour.PonyArmourModel;
import com.minelittlepony.sockies.client.SocksFeature;
import com.minelittlepony.sockies.compat.trinkets.Trinkets;
import com.minelittlepony.sockies.item.SocksItem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SocksGear implements Gear {
    private final PonyArmourModel<LivingEntity> model = ModelType.INNER_PONY_ARMOR.createModel();

    private ItemStack socks;

    @Override
    public boolean canRender(PonyModel<?> model, Entity entity) {
        return entity instanceof LivingEntity living && Trinkets.getSocks(living).findFirst().isPresent();
    }

    @Override
    public BodyPart getGearLocation() {
        return BodyPart.LEGS;
    }

    @Override
    public <T extends Entity> Identifier getTexture(T entity, Context<T, ?> context) {
        return SocksFeature.getTexture((SocksItem)socks.getItem(), ((SocksItem)socks.getItem()).getPattern(), 0);
    }

    @Override
    public <M extends EntityModel<?> & PonyModel<?>> void transform(M model, MatrixStack matrices) {
        // noop
    }

    @SuppressWarnings("unchecked")
    @Override
    public void pose(PonyModel<?> model, Entity entity, boolean rainboom, UUID interpolatorId, float move, float swing, float bodySwing, float ticks) {
        this.model.poseModel((LivingEntity)entity, swing, move, 0, 0, 0, EquipmentSlot.FEET, ArmourLayer.OUTER, (PonyModel<LivingEntity>)model);
        socks = Trinkets.getSocks((LivingEntity)entity).findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public void render(MatrixStack stack, VertexConsumer vertices, int overlay, int light, int color, UUID interpolatorId) {
        Immediate provider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        SocksFeature.renderSocks(socks, model, stack, provider, overlay);
    }
}
