package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.projectile.FishingHook;

@Mixin(FishingHook.class)
public interface FishHookAccessor {
	/*@Accessor("currentState")
	Enum<?> getCurrentState();*/
}
