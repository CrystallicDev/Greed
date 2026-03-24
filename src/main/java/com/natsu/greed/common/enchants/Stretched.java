package com.natsu.greed.common.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Stretched extends Enchantment {

	public Stretched() {
		super(Rarity.COMMON, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public int getMaxLevel() {
		return 2;
	}
	
}