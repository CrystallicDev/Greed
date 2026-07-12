package com.natsu.greed;


import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.common.registry.GreedLootModifiers;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.GreedEnchantModifiers;
import com.natsu.greed.server.food.GreedFoodModifiers;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.events.CartographerTradesInitEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Greed.MODID)
public class Greed {
	public static final String MODID = "greed";

    public Greed() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	GreedEnchants.ENCHANTMENTS.register(modEventBus);
    	GreedLootModifiers.SERIALIZERS.register(modEventBus);
    	GreedBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    	GreedBlocks.BLOCKS.register(modEventBus);
    	ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC);
    	modEventBus.addListener(Greed::onConfigLoading);
    	modEventBus.addListener(Greed::onConfigReloading);
    	MixinExtrasBootstrap.init();
    }

    // La config SERVER n'est chargée qu'à l'ouverture d'un monde : appliquer les
    // modificateurs avant provoquerait une IllegalStateException.
    private static void onConfigLoading(final ModConfigEvent.Loading event) {
    	applyConfigModifiers(event);
    }

    private static void onConfigReloading(final ModConfigEvent.Reloading event) {
    	applyConfigModifiers(event);
    }

    private static void applyConfigModifiers(ModConfigEvent event) {
    	if (event.getConfig().getSpec() == ServerConfig.SPEC) {
    		GreedEnchantModifiers.init();
    		GreedFoodModifiers.init();
    	}
    }


}
