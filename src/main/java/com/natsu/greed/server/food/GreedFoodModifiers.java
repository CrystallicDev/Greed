package com.natsu.greed.server.food;

import java.util.List;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.GatherComponentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * Farmer's Delight : réduit la nutrition des viandes cuites simples.
 *
 * <p>1.20.6 : la nourriture n'est plus un champ mutable de l'Item mais le composant
 * {@link DataComponents#FOOD}. On patche donc les composants par défaut au moment de leur
 * construction via {@link GatherComponentsEvent.Item} (bus Forge). Le rééquilibrage n'est plus
 * re-piloté à chaud par la config SERVER (non chargée à ce stade) : on applique le facteur par défaut.
 */
@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GreedFoodModifiers {

	private static final List<Item> PLAIN_COOKED_MEATS = List.of(
			Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP);

	@SubscribeEvent
	public static void onGatherComponents(GatherComponentsEvent.Item event) {
		if (!ModList.get().isLoaded("farmersdelight")) return;
		if (!ServerConfig.USE_FD_COOKED_MEAT_REBALANCE.getDefault()) return;
		if (!PLAIN_COOKED_MEATS.contains(event.getOwner())) return;

		FoodProperties original = event.getDataComponentMap().get(DataComponents.FOOD);
		if (original == null) return;

		double factor = ServerConfig.FD_COOKED_MEAT_NUTRITION_FACTOR.getDefault();
		FoodProperties reduced = new FoodProperties.Builder()
				.nutrition(Math.max(1, (int) Math.round(original.nutrition() * factor)))
				.saturationModifier(original.saturation() * (float) factor)
				.build();
		event.register(DataComponents.FOOD, reduced);
	}

}
