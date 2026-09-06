package com.thevortex.potionsmaster.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;


public class Pestle extends Item {

    public Pestle(Properties properties) {

        super(properties.stacksTo(1));

    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        return new ItemStackTemplate(this);
    }


}
