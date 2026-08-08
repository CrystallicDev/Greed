package com.natsu.greed.utils;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

public class PotionCreatorUtils {

	// 1.21 : plus de NBT CustomPotionColor/CustomPotionEffects, tout passe par le composant POTION_CONTENTS.
	public static ItemStack makeIntoPotion(Item item, List<MobEffectInstance> effects, int color) {
		ItemStack stack = new ItemStack(item);
		PotionContents contents = new PotionContents(Optional.empty(), Optional.of(color), List.copyOf(effects));
		stack.set(DataComponents.POTION_CONTENTS, contents);
		return stack;
	}

	public static ItemStack makeIntoPotion(Item item, List<MobEffectInstance> effects) {
		return makeIntoPotion(item, effects, PotionContents.getColor(effects));
	}

}
