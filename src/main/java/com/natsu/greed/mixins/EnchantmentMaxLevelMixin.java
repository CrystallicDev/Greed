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

// Enchantments are data-driven now, so there's no BuiltInRegistries.ENCHANTMENT to reverse-look the
// id. We go through the running server's registry access, which also means we only touch the
// server-side value (the client just uses whatever the server synced, so no mismatch).
@Mixin(value = Enchantment.class, remap = false)
public abstract class EnchantmentMaxLevelMixin {

	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int greed$modifyMaxLevel(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_LEVELS.get()) { return original; }
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) { return original; }
		Registry<Enchantment> registry = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		Identifier id = registry.getKey((Enchantment) (Object) this);
		if (id == null) { return original; }
		Map<String, Integer> map = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_LEVELS.get());
		Integer override = map.get(id.toString());
		return override != null ? override : original;
	}

}
