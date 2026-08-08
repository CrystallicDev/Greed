package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class CurseOfCreeping extends Enchantment {
	public CurseOfCreeping() {
		super(Enchantment.definition(ItemTags.FOOT_ARMOR_ENCHANTABLE, 2, 1,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, EquipmentSlot.FEET));
	}
	@Override public boolean isCurse() { return true; }
}
