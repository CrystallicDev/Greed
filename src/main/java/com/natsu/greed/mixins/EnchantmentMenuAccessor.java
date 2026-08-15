package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;

// runtime is Mojmap, so remap=false and the @Accessor names target the Mojmap fields.
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
