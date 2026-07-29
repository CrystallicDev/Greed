package com.natsu.greed.server.food.events;

import java.util.Set;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;

// Farmer's Delight : viande crue et pain non consommables tels quels
@EventBusSubscriber(modid = Greed.MODID)
public class RawFoodListener {

	private static final Set<Item> BLOCKED_RAW_MEATS = Set.of(
			Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP);

	@SubscribeEvent
	public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
		if (!ModList.get().isLoaded("farmersdelight")) return;

		Item item = event.getItem().getItem();
		if (BLOCKED_RAW_MEATS.contains(item) && ServerConfig.USE_FD_RAW_MEAT_REBALANCE.get()) {
			block(event, "message.greed.raw_meat");
		} else if (item == Items.BREAD && ServerConfig.USE_FD_BREAD_REBALANCE.get()) {
			block(event, "message.greed.plain_bread");
		}
	}

	private static void block(LivingEntityUseItemEvent.Start event, String messageKey) {
		event.setCanceled(true);
		if (event.getEntity() instanceof ServerPlayer player) {
			// 26.1 : displayClientMessage → sendSystemMessage(Component, boolean overlay).
			player.sendSystemMessage(Component.translatable(messageKey), true);
		}
	}

}
