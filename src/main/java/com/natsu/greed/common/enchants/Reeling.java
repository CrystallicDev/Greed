package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class Reeling extends Enchantment {
	public Reeling() {
		super(Enchantment.definition(ItemTags.FISHING_ENCHANTABLE, 10, 4,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 1, EquipmentSlot.MAINHAND));
	}
}
