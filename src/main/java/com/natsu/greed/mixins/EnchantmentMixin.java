package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

// Override du max level / max cost des enchantements vanilla (config).
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int modifyMaxLevel(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_LEVELS.get()) { return original; }

		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
		if (id == null) return original;
		if (!id.getNamespace().equals("minecraft")) return original;
		Map<String, Integer> maxLevelMap = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_LEVELS.get());
		Integer override = maxLevelMap.get(id.toString());
		return override != null ? override : original;
	}

	@ModifyReturnValue(method = "getMaxCost", at = @At("RETURN"))
	private int modifyMaxCost(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_COST.get()) { return original; }

		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
		if (id == null) return original;
		if (!id.getNamespace().equals("minecraft")) return original;
		Map<String, Integer> maxLevelMap = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_COST.get());
		Integer override = maxLevelMap.get(id.toString());
		return override != null ? override : original;
	}

}
