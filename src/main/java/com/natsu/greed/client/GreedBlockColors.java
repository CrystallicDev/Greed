package com.natsu.greed.client;

import java.util.List;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// tints the cauldron water based on the potions inside
@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
