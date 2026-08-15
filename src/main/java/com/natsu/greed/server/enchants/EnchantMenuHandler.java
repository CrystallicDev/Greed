package com.natsu.greed.server.enchants;

import java.util.ArrayList;
import java.util.List;

import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class EnchantMenuHandler {

	// resolve the config's curse keys through the static registry and keep only
	// the ones that actually apply to the item.
	public static List<EnchantmentInstance> onInterceptEnchant(EnchantmentTableState state, RandomSource rng,
			ItemStack item, List<EnchantmentInstance> original) {
		if (state == EnchantmentTableState.DEFAULT && item.getItem() == Items.BOOK) return new ArrayList<>();

		List<EnchantmentInstance> modified = new ArrayList<>();
		List<Enchantment> curses = resolveCurses(state, item);
		List<EnchantmentInstance> allowedEnchants = filterEnchants(state, original);
		if (allowedEnchants.isEmpty()) return allowedEnchants;

		// curses can be empty (empty config, or no curse fits the item), so nothing gets rolled.
		boolean rollCurse = !curses.isEmpty() && rng.nextFloat() <= ServerConfig.getCurseProbability(state);

		if (state == EnchantmentTableState.DEFAULT) {
			if (rollCurse) {
				modified.add(new EnchantmentInstance(curses.get(rng.nextInt(curses.size())), 1));
			} else {
				modified.add(new EnchantmentInstance(allowedEnchants.get(0).enchantment, 1));
			}
		} else if (state == EnchantmentTableState.LAPIS_STATE) {
			for (EnchantmentInstance ench : allowedEnchants) {
				modified.add(new EnchantmentInstance(ench.enchantment, 1));
			}
			if (rollCurse) {
				modified.add(new EnchantmentInstance(curses.get(rng.nextInt(curses.size())), 1));
			}
		} else if (state == EnchantmentTableState.AMETHYST_STATE) {
			modified.addAll(allowedEnchants);
			if (rollCurse) {
				modified.add(new EnchantmentInstance(curses.get(rng.nextInt(curses.size())), 1));
			}
		}
		return modified;
	}

	private static List<Enchantment> resolveCurses(EnchantmentTableState state, ItemStack item) {
		List<Enchantment> curses = new ArrayList<>();
		for (ResourceKey<Enchantment> key : ServerConfig.getCurseList(state)) {
			Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(key);
			if (ench != null && ench.canEnchant(item)) curses.add(ench);
		}
		return curses;
	}

	private static List<EnchantmentInstance> filterEnchants(EnchantmentTableState state, List<EnchantmentInstance> original) {
		List<EnchantmentInstance> allowed = new ArrayList<>();
		boolean isWhiteList = ServerConfig.isWhiteList(state);
		List<ResourceKey<Enchantment>> stageEnchants = ServerConfig.getEnchantmentList(state);
		for (EnchantmentInstance instance : original) {
			boolean listed = BuiltInRegistries.ENCHANTMENT.getResourceKey(instance.enchantment)
					.map(stageEnchants::contains).orElse(false);
			// whitelist: keep the listed enchants; blacklist: keep the ones not listed.
			if (listed == isWhiteList) {
				allowed.add(instance);
			}
		}
		return allowed;
	}

}
