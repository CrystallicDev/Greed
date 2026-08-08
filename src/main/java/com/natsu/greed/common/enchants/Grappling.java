package com.natsu.greed.common.enchants;
import com.natsu.greed.common.registry.GreedEnchants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
public class Grappling extends Enchantment {
	public Grappling() {
		super(Enchantment.definition(ItemTags.FISHING_ENCHANTABLE, 1, 3,
				Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 8, EquipmentSlot.MAINHAND));
	}
	@Override protected boolean checkCompatibility(Enchantment e) { return this != e && e != GreedEnchants.REELING.get(); }
}
