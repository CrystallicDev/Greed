package com.natsu.greed;


import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.common.registry.GreedLootModifiers;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Greed.MODID)
public class Greed {
	public static final String MODID = "greed";

    public Greed() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	GreedEnchants.ENCHANTMENTS.register(modEventBus);
    	GreedLootModifiers.SERIALIZERS.register(modEventBus);
    	MixinExtrasBootstrap.init();
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    	
    }

}
