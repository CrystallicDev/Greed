package com.natsu.greed.client;

import java.util.List;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Teinte le liquide du chaudron Greed selon la combinaison de potions qu'il contient
 * (même calcul de couleur que les fioles de potion). Le modèle vanilla du chaudron
 * d'eau expose un tintindex sur la face du liquide.
 */
@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GreedBlockColors {

	@SubscribeEvent
	public static void onBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, getter, pos, tintIndex) -> {
			if (getter != null && pos != null
					&& getter.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron) {
				return PotionUtils.getColor(cauldron.getEffects());
			}
			return PotionUtils.getColor(List.of());
		}, GreedBlocks.CAULDRON.get());
	}

}
