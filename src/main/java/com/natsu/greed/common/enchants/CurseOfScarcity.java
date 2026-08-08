package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class CurseOfScarcity extends Enchantment {
	public CurseOfScarcity() {
		super(Enchantment.definition(ItemTags.SWORD_ENCHANTABLE, 2, 3,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, EquipmentSlot.MAINHAND));
	}
	@Override public boolean isCurse() { return true; }
	@Override protected boolean checkCompatibility(Enchantment e) { return this != e && e != Enchantments.LOOTING; }
}
