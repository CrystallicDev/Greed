package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

// getMaxCost(int) is final on Enchantment, so targeting the base class is enough.
@Mixin(value = Enchantment.class, remap = false)
public abstract class EnchantmentMaxCostMixin {

	@ModifyReturnValue(method = "getMaxCost", at = @At("RETURN"))
	private int greed$modifyMaxCost(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_COST.get()) { return original; }
		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(self);
		if (id == null || !id.getNamespace().equals("minecraft")) return original;
		Map<String, Integer> map = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_COST.get());
		Integer override = map.get(id.toString());
		return override != null ? override : original;
	}

}
