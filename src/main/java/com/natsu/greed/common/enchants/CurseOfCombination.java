package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class CurseOfCombination extends Enchantment {
	public CurseOfCombination() {
		super(Enchantment.definition(ItemTags.DURABILITY_ENCHANTABLE, 2, 1,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4));
	}
	@Override public boolean isCurse() { return true; }
}
