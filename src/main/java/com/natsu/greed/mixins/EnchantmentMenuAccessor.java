package com.natsu.greed.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.level.block.AbstractCauldronBlock;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentMenuAccessor {
	@Accessor
    ContainerLevelAccess getAccess();
	@Accessor
    Random getRandom();
	@Accessor
    int[] getCosts();
}