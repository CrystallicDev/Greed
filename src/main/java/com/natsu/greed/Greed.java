package com.natsu.greed;


import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.common.registry.GreedLootModifiers;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.GreedEnchantModifiers;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.events.CartographerTradesInitEvent;

import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Greed.MODID)
public class Greed {
	public static final String MODID = "greed";

    public Greed() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	GreedEnchants.ENCHANTMENTS.register(modEventBus);
    	GreedLootModifiers.SERIALIZERS.register(modEventBus);
    	GreedBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    	ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC);
    	MixinExtrasBootstrap.init();
    	if (!DatagenModLoader.isRunningDataGen()) {
        	GreedEnchantModifiers.init();
    	}
    }


}
