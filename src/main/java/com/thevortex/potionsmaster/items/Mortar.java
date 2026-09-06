package com.thevortex.potionsmaster.items;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class Mortar extends BlockItem {

    public Mortar(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        return new ItemStackTemplate(this);
    }

}
