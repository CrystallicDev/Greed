package com.natsu.greed.server.food;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.mixins.ItemAccessor;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;

/**
 * Intégration Farmer's Delight : réduit la valeur nutritive des viandes cuites
 * "simples" pour rendre les plats de Farmer's Delight plus intéressants.
 * Appelé au chargement de la config SERVER (les valeurs d'origine sont conservées
 * pour pouvoir restaurer si l'option est désactivée).
 */
public class GreedFoodModifiers {

	private static final List<Item> PLAIN_COOKED_MEATS = List.of(
			Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP);

	private static final Map<Item, FoodProperties> ORIGINALS = new HashMap<>();

	public static void init() {
		if (!ModList.get().isLoaded("farmersdelight")) {
			return;
		}
		boolean enabled = ServerConfig.USE_FD_COOKED_MEAT_REBALANCE.get();
		double factor = ServerConfig.FD_COOKED_MEAT_NUTRITION_FACTOR.get();

		for (Item item : PLAIN_COOKED_MEATS) {
			FoodProperties original = ORIGINALS.computeIfAbsent(item, Item::getFoodProperties);
			if (original == null) {
				continue;
			}
			if (!enabled) {
				((ItemAccessor) item).setFoodProperties(original);
				continue;
			}
			FoodProperties.Builder reduced = new FoodProperties.Builder()
					.nutrition(Math.max(1, (int) Math.round(original.getNutrition() * factor)))
					.saturationMod(original.getSaturationModifier() * (float) factor);
			if (original.isMeat()) {
				reduced.meat();
			}
			if (original.canAlwaysEat()) {
				reduced.alwaysEat();
			}
			if (original.isFastFood()) {
				reduced.fast();
			}
			((ItemAccessor) item).setFoodProperties(reduced.build());
		}
	}

}
