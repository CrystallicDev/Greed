package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(targets = {
		"net.minecraft.world.item.enchantment.ProtectionEnchantment",
		"net.minecraft.world.item.enchantment.OxygenEnchantment",
		"net.minecraft.world.item.enchantment.WaterWorkerEnchantment",
		"net.minecraft.world.item.enchantment.ThornsEnchantment",
		"net.minecraft.world.item.enchantment.WaterWalkerEnchantment",
		"net.minecraft.world.item.enchantment.FrostWalkerEnchantment",
		"net.minecraft.world.item.enchantment.BindingCurseEnchantment",
		"net.minecraft.world.item.enchantment.SoulSpeedEnchantment",
		"net.minecraft.world.item.enchantment.DamageEnchantment",
		"net.minecraft.world.item.enchantment.KnockbackEnchantment",
		"net.minecraft.world.item.enchantment.FireAspectEnchantment",
		"net.minecraft.world.item.enchantment.LootBonusEnchantment",
		"net.minecraft.world.item.enchantment.SweepingEdgeEnchantment",
		"net.minecraft.world.item.enchantment.DiggingEnchantment",
		"net.minecraft.world.item.enchantment.UntouchingEnchantment",
		"net.minecraft.world.item.enchantment.DigDurabilityEnchantment",
		"net.minecraft.world.item.enchantment.ArrowDamageEnchantment",
		"net.minecraft.world.item.enchantment.ArrowKnockbackEnchantment",
		"net.minecraft.world.item.enchantment.ArrowFireEnchantment",
		"net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment",
		"net.minecraft.world.item.enchantment.FishingSpeedEnchantment",
		"net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment",
		"net.minecraft.world.item.enchantment.TridentImpalerEnchantment",
		"net.minecraft.world.item.enchantment.TridentRiptideEnchantment",
		"net.minecraft.world.item.enchantment.TridentChannelingEnchantment",
		"net.minecraft.world.item.enchantment.MultiShotEnchantment",
		"net.minecraft.world.item.enchantment.QuickChargeEnchantment",
		"net.minecraft.world.item.enchantment.ArrowPiercingEnchantment",
		"net.minecraft.world.item.enchantment.MendingEnchantment",
		"net.minecraft.world.item.enchantment.VanishingCurseEnchantment"
})
public abstract class EnchantmentMixin {

	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int modifyMaxLevel(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_LEVELS.get()) { return original; }
		
	    Enchantment self = (Enchantment)(Object) this;
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
		
	    Enchantment self = (Enchantment)(Object) this;
	    ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
	    if (id == null) return original;
	    if (!id.getNamespace().equals("minecraft")) return original;
	    Map<String, Integer> maxLevelMap = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_COST.get());
	    Integer override = maxLevelMap.get(id.toString());
	    return override != null ? override : original;
	}
	
}
