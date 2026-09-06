package com.thevortex.potionsmaster.tint;

import com.mojang.serialization.MapCodec;
import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.render.util.BlockData;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record OresightPowderTintSource() implements ItemTintSource {
	public static final MapCodec<OresightPowderTintSource> CODEC = MapCodec.unit(OresightPowderTintSource::new);

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
		String path = id.getPath();
		boolean calcinated = path.startsWith("calcinated_");
		String entryName = calcinated
				? path.substring("calcinated_".length(), path.length() - "_oresight_powder".length())
				: path.substring(0, path.length() - "_oresight_powder".length());

		BlockData data = PotionsMaster.blockStore.getByEntryName(entryName);
		return data != null ? data.getColor() : -1;
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return CODEC;
	}
}