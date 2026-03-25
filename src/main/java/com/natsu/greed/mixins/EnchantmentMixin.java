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
		if (!ServerConfig.USE_CUSTOM_MAX_LEVELS.get()) { return original; }
		
	    Enchantment self = (Enchantment)(Object) this;
	    ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
	    System.out.println("Setting Enchant Max Level for : "+self+" with ResourceLocation "+(id));
	    if (id == null) return original;
	    if (!id.getNamespace().equals("minecraft")) return original;
	    System.out.println("Current Targeted Enchantment: "+id.getNamespace()+":"+id.getPath()+", searching for : "+id.toString());
	    Map<String, Integer> maxLevelMap = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_LEVELS.get());
	    Integer override = maxLevelMap.get(id.toString());

	    return override != null ? override : original;
	}
	
}
