package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public interface ItemAccessor {

	@Mutable
	@Accessor("foodProperties")
	void setFoodProperties(FoodProperties properties);

}
