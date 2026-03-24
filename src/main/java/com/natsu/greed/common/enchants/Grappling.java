package com.natsu.greed.common.enchants;

import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Grappling extends Enchantment {

	public Grappling() {
		super(Rarity.VERY_RARE, EnchantmentCategory.FISHING_ROD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}
	
	@Override
	protected boolean checkCompatibility(Enchantment enchant) {
		return this != enchant && enchant != GreedEnchants.REELING.get();
	}
	
}