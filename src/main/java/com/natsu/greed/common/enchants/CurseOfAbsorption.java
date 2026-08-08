package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
// 1.20.6 : Enchantment reste extensible mais se construit via une EnchantmentDefinition (tags + coûts).
public class CurseOfAbsorption extends Enchantment {
	public CurseOfAbsorption() {
		super(Enchantment.definition(ItemTags.ARMOR_ENCHANTABLE, 2, 1,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	}
	@Override public boolean isCurse() { return true; }
	@Override protected boolean checkCompatibility(Enchantment e) { return this != e && e != Enchantments.MENDING; }
}
