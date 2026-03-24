package com.natsu.greed.server.fishing;

import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class GrappleHandler {

	public static void handleGrapple(FishingHook hook, Player player) {
        double dx = hook.getX() - player.getX();
        double dy = hook.getY() - player.getY();
        double dz = hook.getZ() - player.getZ();
        if (!hook.isOnGround()) {
        	return;
        }
        int level = EnchantmentHelper.getEnchantmentLevel(GreedEnchants.GRAPPLING.get(), player);
        double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
        double speed = 1.2 + (0.2 * level - 1);

        player.setDeltaMovement(
            (dx / distance) * speed,
            (dy / distance) * speed + 0.1,
            (dz / distance) * speed
        );
        player.hasImpulse = true;
        hook.discard();
    }
	
}
