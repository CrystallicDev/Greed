package com.natsu.greed.server.fishing;

import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class GrappleHandler {

	public static void handleGrapple(FishingHook hook, Player player) {
	    if (player.level.isClientSide) return;

	    double dx = hook.getX() - player.getX();
	    double dy = hook.getY() - player.getY();
	    double dz = hook.getZ() - player.getZ();

	    double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
	    if (distance < 0.001) return;

	    if (!hook.horizontalCollision && !hook.verticalCollision) return;

	    int level = EnchantmentHelper.getEnchantmentLevel(GreedEnchants.GRAPPLING.get(), player);
	    double speed = 1.5 + (0.5 * (level - 1));

	    player.setDeltaMovement(
	        (dx / distance) * speed,
	        (dy / distance) * speed + 0.2,
	        (dz / distance) * speed
	    );

	    player.setOnGround(false);
	    player.hasImpulse = true;
	    player.hurtMarked = true;

	    hook.discard();
	}
	
}
