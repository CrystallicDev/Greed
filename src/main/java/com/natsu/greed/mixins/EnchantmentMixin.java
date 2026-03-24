package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int modifyMaxLevel(int original) {
	    Enchantment self = (Enchantment)(Object) this;
	    ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
	    if (id == null) return original;
	    if (!id.getNamespace().equals("minecraft")) return original;

	    Map<String, Integer> maxLevelMap = ServerConfig.ENCHANTMENTS_MAX_LEVELS.get();
	    Integer override = maxLevelMap.get(id.toString());

	    return override != null ? override : original;
	}
	
}
