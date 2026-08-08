package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class CurseOfTheSponge extends Enchantment {
	public CurseOfTheSponge() {
		super(Enchantment.definition(ItemTags.ARMOR_ENCHANTABLE, 5, 1,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 2, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	}
	@Override public boolean isCurse() { return true; }
}
