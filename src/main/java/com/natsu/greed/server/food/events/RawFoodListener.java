package com.natsu.greed.server.food.events;

import java.util.Set;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * Intégration Farmer's Delight : la viande crue (boeuf, poulet, mouton, porc) ne
 * peut plus être mangée telle quelle. Elle doit être découpée puis cuite via les
 * stations de Farmer's Delight. Les poissons et autres aliments sont inchangés.
 */
@Mod.EventBusSubscriber(modid = Greed.MODID)
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
		if (event.getEntityLiving() instanceof ServerPlayer player) {
			player.displayClientMessage(new TranslatableComponent(messageKey), true);
		}
	}

}
