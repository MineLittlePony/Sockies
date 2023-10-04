package com.minelittlepony.sockies.compat.minelittlepony;

import java.util.UUID;

import com.minelittlepony.api.model.BodyPart;
import com.minelittlepony.api.model.IModel;
import com.minelittlepony.api.model.armour.ArmourLayer;
import com.minelittlepony.api.model.gear.IGear;
import com.minelittlepony.client.model.IPonyModel;
import com.minelittlepony.client.model.ModelType;
import com.minelittlepony.client.model.armour.PonyArmourModel;
import com.minelittlepony.sockies.STags;
import com.minelittlepony.sockies.client.SocksFeature;
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

public class SocksGear implements IGear {
    private final PonyArmourModel<LivingEntity> model = ModelType.INNER_PONY_ARMOR.createModel();

    private ItemStack socks;

    @Override
    public boolean canRender(IModel model, Entity entity) {
        return entity instanceof LivingEntity living && living.getEquippedStack(EquipmentSlot.FEET).isIn(STags.SOCKS);
    }

    @Override
    public BodyPart getGearLocation() {
        return BodyPart.LEGS;
    }

    @Override
    public <T extends Entity> Identifier getTexture(T entity, Context<T, ?> context) {
        return SocksFeature.getTexture((SocksItem)socks.getItem(), ((SocksItem)socks.getItem()).getPattern(), 1);
    }

    @Override
    public <M extends EntityModel<?> & IPonyModel<?>> void transform(M model, MatrixStack matrices) {
        // noop
    }

    @SuppressWarnings("unchecked")
    @Override
    public void pose(IModel model, Entity entity, boolean rainboom, UUID interpolatorId, float move, float swing, float bodySwing, float ticks) {
        if (model instanceof IPonyModel<?> lmodel) {
            this.model.poseModel((LivingEntity)entity, swing, move, 0, 0, 0, EquipmentSlot.FEET, ArmourLayer.OUTER, (IPonyModel<LivingEntity>)lmodel);
        }
        socks = ((LivingEntity)entity).getEquippedStack(EquipmentSlot.FEET);
    }

    @Override
    public void render(MatrixStack stack, VertexConsumer vertices, int overlayUv, int lightUv, float red, float green, float blue, float alpha, UUID interpolatorId) {
        Immediate provider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        SocksFeature.renderSocks(socks, model, stack, provider, overlayUv);
    }
}
