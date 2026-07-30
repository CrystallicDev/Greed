package com.natsu.greed.server.fishing;

import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class GrappleHandler {

	public static void handleGrapple(FishingHook hook, Player player) {
	    if (player.level().isClientSide()) return;

	    double dx = hook.getX() - player.getX();
	    double dy = hook.getY() - player.getY();
	    double dz = hook.getZ() - player.getZ();

	    double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
	    if (distance < 0.001) return;

	    // Le hook fige sa vélocité en se posant (gravité coupée dès onGround) → verticalCollision
	    // redevient false. On teste onGround() (stable) au lieu de verticalCollision, sinon le grapple
	    // n'accroche que tant que le bobber bouge encore (impression de distance minimale).
	    if (!hook.horizontalCollision && !hook.onGround()) return;

	    Holder<Enchantment> grappling = GreedEnchants.get(player.registryAccess(), GreedEnchants.GRAPPLING);
	    int level = grappling == null ? 0 : EnchantmentHelper.getEnchantmentLevel(grappling, player);
	    double speed = 1.5 + (0.5 * (level - 1));

	    player.setDeltaMovement(
	        (dx / distance) * speed,
	        (dy / distance) * speed + 0.2,
	        (dz / distance) * speed
	    );

	    player.setOnGround(false);
	    player.needsSync = true;
	    player.hurtMarked = true;

	    hook.discard();
	}
	
}
