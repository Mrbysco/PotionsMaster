package com.thevortex.potionsmaster.datagen;

import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.client.DynamicEffectSpriteSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ModDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(PotionSpriteProvider::new);
	}

	public static class PotionSpriteProvider extends SpriteSourceProvider {
		public PotionSpriteProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider, PotionsMaster.MOD_ID);
		}

		@Override
		protected void gather() {
			atlas(AtlasIds.GUI).addSource(
					new DynamicEffectSpriteSource()
			);
		}
	}
}
