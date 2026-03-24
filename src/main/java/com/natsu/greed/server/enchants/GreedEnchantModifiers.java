package com.natsu.greed.server.enchants;

import java.util.Map;

import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.mixins.EnchantmentAccessor;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class GreedEnchantModifiers {

	public static void init() {
		Map<String, Integer> rarityMap = ServerConfig.ENCHANTMENTS_RARITY.get();

	    for (Map.Entry<String, Integer> entry : rarityMap.entrySet()) {
	        ResourceLocation id = new ResourceLocation(entry.getKey());

	        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
	        if (enchantment == null) continue;

	        Enchantment.Rarity rarity = ServerConfig.intToRarity(entry.getValue());
	        ((EnchantmentAccessor) enchantment).setRarity(rarity);
	    }
	}
	
}
