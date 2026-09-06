package com.thevortex.potionsmaster.items.potions.effect.oresight;

import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.network.OreSightEnablePacket;
import com.thevortex.potionsmaster.network.PacketHandler;
import com.thevortex.potionsmaster.reference.Reference;
import com.thevortex.potionsmaster.render.util.BlockData;
import com.thevortex.potionsmaster.render.util.BlockStore.BlockDataWithUUID;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class OreSightEffect extends MobEffect {
    protected String effectType;
    public OreSightEffect(MobEffectCategory typeIn, String effectType, int liquidColorIn) {
        super(typeIn, liquidColorIn);
        this.effectType = effectType;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {

        return duration > 0;
    }

    public String getEffectType(){
        return this.effectType;
    }


    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            BlockDataWithUUID bdUUID = PotionsMaster.blockStore.getStoreByReference(this.effectType);
            if (bdUUID != null) {
                BlockData oreSight = bdUUID.getBlockData();
                if (!oreSight.isDrawing()) {
                    oreSight.setDrawing(true);
                    PacketHandler.sendTo(new OreSightEnablePacket(this.effectType), serverPlayer);
                }
            }
        }
        return super.applyEffectTick(serverLevel, livingEntity, amplifier);
    }
}
