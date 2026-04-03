package com.natsu.greed.utils;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PotionCreatorUtils {

	public static ItemStack makeIntoPotion(Item item, List<MobEffectInstance> effects, int color) {
	    ItemStack stack = new ItemStack(item);
	    CompoundTag tag = stack.getOrCreateTag();
	    ListTag effectsList = new ListTag();
	    for (MobEffectInstance effect : effects) {
	        CompoundTag effectTag = new CompoundTag();
	        effect.save(effectTag);
	        effectsList.add(effectTag);
	    }
	    tag.putInt("CustomPotionColor", color);
	    tag.put("CustomPotionEffects", effectsList);
	    return stack;
	}
	
	public static ItemStack makeIntoPotion(Item item, List<MobEffectInstance> effects) {
	    ItemStack stack = new ItemStack(item);
	    CompoundTag tag = stack.getOrCreateTag();
	    ListTag effectsList = new ListTag();
	    int color = getColorFor(effects);
	    for (MobEffectInstance effect : effects) {
	        CompoundTag effectTag = new CompoundTag();
	        effect.save(effectTag);
	        effectsList.add(effectTag);
	    }
	    tag.putInt("CustomPotionColor", color);
	    tag.put("CustomPotionEffects", effectsList);
	    return stack;
	}

	private static int getColorFor(List<MobEffectInstance> effects) {
		int color = 0;
		for (MobEffectInstance effect : effects) {
			if (effect.getEffect().isBeneficial())
				color += effect.getEffect().getColor();
			else 
				color -= effect.getEffect().getColor();
		}
		return color;
	}
	
}
