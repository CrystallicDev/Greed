package com.natsu.greed.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

// Meme logique que EnchantmentMaxLevelMixin mais pour getMaxCost, overridé sur un jeu de
// classes plus large (d'ou la liste distincte).
@Mixin(targets = {
		"net.minecraft.world.item.enchantment.Enchantment",
		"net.minecraft.world.item.enchantment.ProtectionEnchantment",
		"net.minecraft.world.item.enchantment.OxygenEnchantment",
		"net.minecraft.world.item.enchantment.WaterWorkerEnchantment",
		"net.minecraft.world.item.enchantment.ThornsEnchantment",
		"net.minecraft.world.item.enchantment.WaterWalkerEnchantment",
		"net.minecraft.world.item.enchantment.FrostWalkerEnchantment",
		"net.minecraft.world.item.enchantment.BindingCurseEnchantment",
		"net.minecraft.world.item.enchantment.SoulSpeedEnchantment",
		"net.minecraft.world.item.enchantment.SwiftSneakEnchantment",
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
public abstract class EnchantmentMaxCostMixin {

	@ModifyReturnValue(method = "getMaxCost", at = @At("RETURN"))
	private int modifyMaxCost(int original) {
		if (!ServerConfig.USE_CUSTOM_MAX_COST.get()) { return original; }

		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);
		if (id == null) return original;
		if (!id.getNamespace().equals("minecraft")) return original;
		Map<String, Integer> maxCostMap = ServerConfig.getMap(ServerConfig.ENCHANTMENTS_MAX_COST.get());
		Integer override = maxCostMap.get(id.toString());
		return override != null ? override : original;
	}

}
