package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

// 1.20.6 : getMaxLevel() est final sur Enchantment (le max vient de l'EnchantmentDefinition), donc
// cibler la classe de base suffit — plus besoin de lister les sous-classes comme en 1.20.1.
@Mixin(value = Enchantment.class, remap = false)
public abstract class EnchantmentMaxLevelMixin {

	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int greed$modifyMaxLevel(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_LEVELS.get()) { return original; }
		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(self);
		if (id == null || !id.getNamespace().equals("minecraft")) return original;
		Map<String, Integer> map = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_LEVELS.get());
		Integer override = map.get(id.toString());
		return override != null ? override : original;
	}

}
