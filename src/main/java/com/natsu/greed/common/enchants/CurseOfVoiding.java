package com.natsu.greed.common.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class CurseOfVoiding extends Enchantment {

	public CurseOfVoiding() {
		super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public boolean isCurse() {
		return true;
	}
	
	@Override
	protected boolean checkCompatibility(Enchantment enchant) {
		return this != enchant && enchant != Enchantments.BLOCK_FORTUNE;
	}

}
