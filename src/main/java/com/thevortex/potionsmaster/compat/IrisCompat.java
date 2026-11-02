package com.thevortex.potionsmaster.compat;

import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.render.util.xray.Render;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisProgram;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class IrisCompat {
    @SubscribeEvent
    private static void clientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("iris")) {
            PotionsMaster.LOGGER.info("Registering Iris pipeline");
            IrisApi.getInstance().assignPipeline(Render.XRAY_PIPELINE, IrisProgram.LINES);
        }
    }
}
