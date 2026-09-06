package com.thevortex.potionsmaster.init;



import net.minecraft.world.food.FoodProperties;

public class ModFoods {


    public static final FoodProperties BEZOAR;
    public static final FoodProperties ACTIVATEDCHARCOAL;
    public static final FoodProperties GALLBLADDER;


    static {
        BEZOAR = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.5F).alwaysEdible().build(); //TODO: Use faster Consumable
        ACTIVATEDCHARCOAL = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.5F).alwaysEdible().build();
        GALLBLADDER = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.5F).alwaysEdible().build();
    }
}
