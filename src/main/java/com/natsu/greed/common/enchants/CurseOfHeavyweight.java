package com.natsu.greed.common.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class CurseOfHeavyweight extends Enchantment {

	public CurseOfHeavyweight() {
		super(Rarity.RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}
	
	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	protected boolean checkCompatibility(Enchantment enchant) {
		return this != enchant && enchant != Enchantments.MENDING;
	}
	
}
