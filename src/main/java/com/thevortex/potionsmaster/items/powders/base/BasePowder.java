package com.thevortex.potionsmaster.items.powders.base;

import net.minecraft.world.item.Item;

public class BasePowder extends Item {
    protected final int color;
    public BasePowder(int color,Properties properties) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}
