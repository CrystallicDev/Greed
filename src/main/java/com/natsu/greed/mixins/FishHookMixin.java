package com.natsu.greed.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.server.fishing.GrappleHandler;

import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FishingHook.class, priority = -9999, remap = false)		// We want to be called last, especially if Pride is loaded
public class FishHookMixin {


    @Inject(method = "retrieve", at = @At("HEAD"), cancellable = true)
    private void onRetrieve(ItemStack rod, CallbackInfoReturnable<Integer> cir) {
        FishingHook self = (FishingHook)(Object) this;
        Player player = self.getPlayerOwner();
        if (player == null) return;
        ItemStack rodStack = player.getMainHandItem();
        Holder<Enchantment> grappling = GreedEnchants.get(self.registryAccess(), GreedEnchants.GRAPPLING);
        if (grappling == null) return;
        int grapplingLevel = EnchantmentHelper.getItemEnchantmentLevel(grappling, rodStack);
        if (grapplingLevel > 0 && self.getHookedIn() == null) {
            GrappleHandler.handleGrapple(self, player);
            cir.setReturnValue(0);
            return;
        }
    }
    
    @Inject(method = "pullEntity", at = @At("HEAD"), cancellable = true)
    private void onPull(Entity pullTarget, CallbackInfo ci) {
        FishingHook self = (FishingHook)(Object) this;
    	Entity entity = self.getOwner();
        if (entity instanceof LivingEntity living) {
        	Holder<Enchantment> reeling = GreedEnchants.get(self.registryAccess(), GreedEnchants.REELING);
        	int level = reeling == null ? 0 : EnchantmentHelper.getEnchantmentLevel(reeling, living);
			if (entity != null) {
				Vec3 vec3 = (new Vec3(entity.getX() - self.getX(), entity.getY() - self.getY(),
						entity.getZ() - self.getZ())).scale(0.1D);
				pullTarget.setDeltaMovement(pullTarget.getDeltaMovement()
						.add(vec3.multiply(1 + (0.3 * level), 1 + (0.2 * level), 1 + (0.3 * level))));
				ci.cancel();
			}
        }
    }
	
    
    
}
