package com.natsu.greed.common.enchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class Light extends Enchantment {
	public Light() {
		super(Enchantment.definition(ItemTags.FISHING_ENCHANTABLE, 10, 3,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 1, EquipmentSlot.MAINHAND));
	}
}
