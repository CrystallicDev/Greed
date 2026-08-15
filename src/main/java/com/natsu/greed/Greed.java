package com.natsu.greed;

import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.common.registry.GreedLootModifiers;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.food.GreedFoodModifiers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Greed.MODID)
public class Greed {

	public static final String MODID = "greed";

	public Greed(IEventBus modEventBus, ModContainer modContainer) {
		GreedLootModifiers.SERIALIZERS.register(modEventBus);
		GreedBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		GreedBlocks.BLOCKS.register(modEventBus);
		modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
		// food rebalance patches the default components at startup (mod bus).
		modEventBus.addListener(GreedFoodModifiers::onModifyComponents);
	}

}
