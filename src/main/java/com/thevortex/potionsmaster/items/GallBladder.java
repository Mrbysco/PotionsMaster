package com.thevortex.potionsmaster.items;

import com.thevortex.potionsmaster.init.ModRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GallBladder extends Item {

    public GallBladder(Properties properties) {
        super(properties.stacksTo(16));

    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {

        if ((entityLiving instanceof Player player) && (stack.getItem() == ModRegistry.GALLBLADDER.get())) {
	        if (player.hasEffect(MobEffects.MINING_FATIGUE)) {
                player.removeEffect(MobEffects.MINING_FATIGUE);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
