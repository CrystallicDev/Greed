package com.natsu.greed.utils;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

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
	    return makeIntoPotion(item, effects, PotionUtils.getColor(effects));
	}

}
