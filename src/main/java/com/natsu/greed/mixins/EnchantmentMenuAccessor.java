package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;

// NeoForge : runtime Mojmap → remap=false, les noms d'@Accessor ciblent les champs Mojmap.
@Mixin(value = EnchantmentMenu.class, remap = false)
public interface EnchantmentMenuAccessor {
	@Accessor
	ContainerLevelAccess getAccess();

	@Accessor
	RandomSource getRandom();

	@Accessor
	int[] getCosts();

	@Accessor
	DataSlot getEnchantmentSeed();
}
