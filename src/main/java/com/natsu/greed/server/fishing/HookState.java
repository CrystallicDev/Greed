package com.natsu.greed.server.fishing;

import com.natsu.greed.mixins.FishHookAccessor;

import net.minecraft.world.entity.projectile.FishingHook;

public enum HookState {
	FLYING, BOBBING, HOOKED_IN_ENTITY;
	
	public static HookState fromHook(FishingHook hook) {
		//Enum<?> state = ((FishHookAccessor)hook).getCurrentState();
		return HookState.values()[0];
	}
}
