package com.thevortex.potionsmaster.events;

import com.thevortex.potionsmaster.PotionsMaster;
import com.thevortex.potionsmaster.client.DynamicEffectSpriteSource;
import com.thevortex.potionsmaster.reference.Reference;
import com.thevortex.potionsmaster.render.util.BlockData;
import com.thevortex.potionsmaster.tint.OresightPowderTintSource;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSpriteSourcesEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.Map;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {


    private static final StandaloneModelKey<QuadCollection> BASE_POWDER_KEY = makeKey("item/base_powder");
    private static final StandaloneModelKey<QuadCollection> CALCINATED_BASE_KEY = makeKey("calcinated_base");

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterStandalone event) {
        // Register the base template models so they get loaded
        PotionsMaster.LOGGER.info("=== Registering Additional Models ===");

        registerModel(event, BASE_POWDER_KEY, "item/base_powder");
        PotionsMaster.LOGGER.info("Registered base_powder model for loading");

        registerModel(event, CALCINATED_BASE_KEY, "item/calcinated_base");
        PotionsMaster.LOGGER.info("Registered calcinated_base model for loading");
        PotionsMaster.LOGGER.info("=== Additional Models Registration Complete ===");
    }

    private static void registerModel(ModelEvent.RegisterStandalone event,
                                      StandaloneModelKey<QuadCollection> key, String path) {
        event.register(key, SimpleUnbakedStandaloneModel.quadCollection(
                PotionsMaster.getId(path)));
    }

    private static StandaloneModelKey<QuadCollection> makeKey(String name) {
        return new StandaloneModelKey<>(() -> Reference.MOD_ID + ":" + name);
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void on(RegisterColorHandlersEvent.ItemTintSources event) {
        PotionsMaster.LOGGER.info("=== Starting Color Handler Registration ===");
        event.register(PotionsMaster.getId("oresight_powder"), OresightPowderTintSource.CODEC);
        PotionsMaster.LOGGER.info("Registered oresight_powder item tint source");
        PotionsMaster.LOGGER.info("=== Color Handler Registration Complete ===");
    }

    @SubscribeEvent
    public static void onRegisterSprites(RegisterSpriteSourcesEvent event) {
        PotionsMaster.LOGGER.info("=== Registering Sprite Source Types ===");
        // Register our custom sprite source type for dynamic effect icons
        event.register(PotionsMaster.getId("dynamic_effect"), DynamicEffectSpriteSource.CODEC);
        PotionsMaster.LOGGER.info("=== Sprite Source Types Registration Complete ===");
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(AddClientReloadListenersEvent event) {
        PotionsMaster.LOGGER.info("=== Registering Language Reload Listener ===");
        event.addListener(PotionsMaster.getId("dynamic_language"), new com.thevortex.potionsmaster.client.DynamicLanguageProvider());
        PotionsMaster.LOGGER.info("=== Language Reload Listener Registered ===");
    }

    @SubscribeEvent
    public static void onTextureAtlasStitch(TextureAtlasStitchedEvent event) {
        // After mob_effect atlas is stitched, verify our sprites were added
        if (event.getAtlas().location().equals(Identifier.withDefaultNamespace("textures/atlas/mob_effects.png"))) {
            PotionsMaster.LOGGER.info("Mob effects atlas stitched - dynamic effect icons should be available");
        }
    }


        @SubscribeEvent
        public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
            Map<Identifier, ItemModel> itemModels = event.getBakingResult().itemStackModels();

            PotionsMaster.LOGGER.info("=== Starting Model Registration ===");
            PotionsMaster.LOGGER.info("Total BlockData entries: " + PotionsMaster.blockStore.getStore().size());

            // Get the base models to clone
            Identifier basePowderLoc = PotionsMaster.getId("base_powder");
            Identifier calcinatedBaseLoc = PotionsMaster.getId("calcinated_base");

            PotionsMaster.LOGGER.info("Looking for base_powder model at: " + basePowderLoc);
            PotionsMaster.LOGGER.info("Looking for calcinated_base model at: " + calcinatedBaseLoc);

            ItemModel basePowderModel = itemModels.get(basePowderLoc);
            ItemModel calcinatedBaseModel = itemModels.get(calcinatedBaseLoc);

            if (basePowderModel == null) {
                PotionsMaster.LOGGER.error("Base powder model not found! Available models:");
                itemModels.keySet().stream()
                        .filter(loc -> loc.getNamespace().equals(Reference.MOD_ID))
                        .limit(20)
                        .forEach(loc -> PotionsMaster.LOGGER.error("  - " + loc));
                return;
            }
            if (calcinatedBaseModel == null) {
                PotionsMaster.LOGGER.error("Calcinated base model not found! Available models:");
                itemModels.keySet().stream()
                        .filter(loc -> loc.getNamespace().equals(Reference.MOD_ID))
                        .limit(20)
                        .forEach(loc -> PotionsMaster.LOGGER.error("  - " + loc));
                return;
            }

            PotionsMaster.LOGGER.info("Successfully found both base models!");

            for(BlockData data : PotionsMaster.blockStore.getStore().values()) {
                // Register regular powder model using base_powder as template;
                itemModels.put(PotionsMaster.getId(data.getEntryName() + "_oresight_powder"), basePowderModel);
                PotionsMaster.LOGGER.info("Registered model for " + data.getEntryName() + "_oresight_powder");

                // Register calcinated powder model using calcinated_base as template
                itemModels.put(PotionsMaster.getId("calcinated_" + data.getEntryName() + "_oresight_powder"), calcinatedBaseModel);
                PotionsMaster.LOGGER.info("Registered model for calcinated_" + data.getEntryName() + "_oresight_powder");
            }

            PotionsMaster.LOGGER.info("=== Model Registration Complete ===");
        }

}

