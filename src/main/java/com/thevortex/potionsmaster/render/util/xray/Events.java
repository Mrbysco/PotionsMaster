package com.thevortex.potionsmaster.render.util.xray;


import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.reference.Reference;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import java.util.Collection;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class Events {


	protected static int counter;

	@SubscribeEvent
	public static void onExit(ServerStoppingEvent event) {
		if ((Controller.drawOres())) {
			Controller.toggleDrawOres();
		}
		Controller.shutdownExecutor();
	}

	
	@SubscribeEvent
	public static void pickupItem(BreakBlockEvent event) {
		RenderEnqueue.checkBlock(event.getPos(), event.getState(), false);
	}

	@SubscribeEvent
	public static void placeItem(BlockEvent.EntityPlaceEvent event) {
		RenderEnqueue.checkBlock(event.getPos(), event.getState(), true);
	}

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		Controller.requestBlockFinder(true);
	}


	@SubscribeEvent
	public static void tickEnd(ClientTickEvent.Post event) {		
		counter++;
		if ((counter > 40) && (Controller.drawOres())) {
			counter = 0;
			Controller.requestBlockFinder(false);
		}
		
	}

	@SubscribeEvent
	public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
		if ((Controller.drawOres()) && (PotionsMaster.proxy.getMinecraft().player != null)) {
			if (PotionsMaster.proxy.getMinecraft().player.getActiveEffects().stream().iterator().hasNext()) {
				boolean pmPot = false;
				Collection<MobEffectInstance> effects = PotionsMaster.proxy.getMinecraft().player.getActiveEffects();
				for (MobEffectInstance effect : effects) {
					if (effect.getEffect().getRegisteredName().contains("potionsmaster")) {
						pmPot = true;
					}
				}

				if (!pmPot) {
					Controller.toggleDrawOres();
					return;
				}

				try {
					Render.INSTANCE.onSubmitCustomGeometry(event);
				} catch (Throwable ignore) {
				}
			}
		}
	}
}
