package com.natsu.greed.common.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Light extends Enchantment {

	public Light() {
		super(Rarity.COMMON, EnchantmentCategory.FISHING_ROD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}
	
}