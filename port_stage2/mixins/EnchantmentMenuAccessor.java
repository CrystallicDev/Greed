package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentMenuAccessor {
	@Accessor
    ContainerLevelAccess getAccess();
	@Accessor
    RandomSource getRandom();
	@Accessor
    int[] getCosts();
}