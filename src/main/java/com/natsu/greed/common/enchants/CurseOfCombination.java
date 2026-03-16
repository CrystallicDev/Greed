package com.natsu.greed.common.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class CurseOfCombination extends Enchantment {

	public CurseOfCombination() {
		super(Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[0]);
	}
	
	@Override
	public boolean isCurse() {
		return true;
	}

}
