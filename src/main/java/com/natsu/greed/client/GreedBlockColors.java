package com.natsu.greed.client;

import java.util.List;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

// Teinte le liquide du chaudron selon les potions qu'il contient
@EventBusSubscriber(modid = Greed.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GreedBlockColors {

	@SubscribeEvent
	public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, getter, pos, tintIndex) -> {
			if (getter != null && pos != null
					&& getter.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron) {
				return PotionContents.getColor(cauldron.getEffects());
			}
			return PotionContents.getColor(List.of());
		}, GreedBlocks.CAULDRON.get());
	}

}
