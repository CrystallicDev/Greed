package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

// getMaxCost(int level) is server-side (enchant power math). We override it to a flat config value,
// ignoring the level, same as the pre-1.21 behaviour.
@Mixin(value = Enchantment.class, remap = false)
public abstract class EnchantmentMaxCostMixin {

	@ModifyReturnValue(method = "getMaxCost", at = @At("RETURN"))
	private int greed$modifyMaxCost(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_COST.get()) { return original; }
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) { return original; }
		Registry<Enchantment> registry = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		Identifier id = registry.getKey((Enchantment) (Object) this);
		if (id == null) { return original; }
		Map<String, Integer> map = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_COST.get());
		Integer override = map.get(id.toString());
		return override != null ? override : original;
	}

}
