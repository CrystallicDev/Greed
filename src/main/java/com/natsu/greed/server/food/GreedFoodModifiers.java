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
 * Farmer's Delight : réduit la nutrition des viandes cuites simples.
 *
 * <p>1.21 : la nourriture n'est plus un champ mutable de l'Item mais le composant
 * {@link DataComponents#FOOD}. On patche donc les composants par défaut au démarrage via
 * {@link ModifyDefaultComponentsEvent} (bus mod). Conséquence vs 1.20.1 : le rééquilibrage n'est
 * plus re-piloté à chaud par la config SERVER (non chargée à ce stade) ; il applique le facteur par
 * défaut. Le pilotage runtime fin est reporté au Bloc B si besoin.
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
