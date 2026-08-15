package com.natsu.greed.server.food;

import java.util.List;

import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

/**
 * Farmer's Delight - drops the nutrition on plain cooked meats.
 *
 * <p>food isn't a mutable Item field anymore, it's the
 * {@link DataComponents#FOOD} component, so we patch the default components at startup via
 * {@link ModifyDefaultComponentsEvent} (mod bus). downside: the rebalance can't be re-driven
 * live by the SERVER config (not loaded yet at this point), so it just applies the default
 * factor. fine-grained runtime control is deferred if we ever need it.
 */
public class GreedFoodModifiers {

	private static final List<Item> PLAIN_COOKED_MEATS = List.of(
			Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP);

	public static void onModifyComponents(ModifyDefaultComponentsEvent event) {
		if (!ModList.get().isLoaded("farmersdelight")) return;
		if (!ServerConfig.USE_FD_COOKED_MEAT_REBALANCE.getDefault()) return;

		double factor = ServerConfig.FD_COOKED_MEAT_NUTRITION_FACTOR.getDefault();
		for (Item item : PLAIN_COOKED_MEATS) {
			FoodProperties original = item.components().get(DataComponents.FOOD);
			if (original == null) continue;

			FoodProperties reduced = new FoodProperties.Builder()
					.nutrition(Math.max(1, (int) Math.round(original.nutrition() * factor)))
					.saturationModifier(original.saturation() * (float) factor)
					.build();
			event.modify(item, patch -> patch.set(DataComponents.FOOD, reduced));
		}
	}

}
