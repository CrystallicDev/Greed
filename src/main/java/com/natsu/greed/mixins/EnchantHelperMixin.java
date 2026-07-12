package com.natsu.greed.mixins;

import net.minecraft.util.RandomSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natsu.greed.config.ServerConfig;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(EnchantmentHelper.class)
public class EnchantHelperMixin {

	@Inject(method = "getEnchantmentCost", at = @At("HEAD"), cancellable = true)
	private static void getEnchantmentCost(RandomSource random, int slot, int enchantPowerBonus, ItemStack itemstack, CallbackInfoReturnable<Integer> ci) {
		if (ServerConfig.DISABLE_BOOKSHELVES_CAP.get()) {
			Item item = itemstack.getItem();
			int i = itemstack.getEnchantmentValue();
			if (i <= 0) {
				ci.setReturnValue(0);;
			} else {
				// Disable cap
				/*if (enchantPowerBonus > 15) {
					enchantPowerBonus = 15;
				}*/

				int offset = random.nextInt(8) + 1 + (enchantPowerBonus >> 1) + random.nextInt(enchantPowerBonus + 1);
				if (slot == 0) {
					ci.setReturnValue(Math.max(offset / 3, 1));
				} else {
					ci.setReturnValue(slot == 1 ? offset * 2 / 3 + 1 : Math.max(offset, enchantPowerBonus * 2));
				}
			}
		}
	}
}
