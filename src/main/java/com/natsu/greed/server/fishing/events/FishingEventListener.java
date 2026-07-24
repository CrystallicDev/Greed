package com.natsu.greed.server.fishing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent.Play;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Greed.MODID)
public class FishingEventListener {
	
	//This should be compatible with Pride's Fishing Hook handling ?
	@SubscribeEvent
	public static void onFishingHook(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof FishingHook hook) {
			if (hook.getOwner() instanceof Player player) {
				if (!event.getLevel().isClientSide && event.getEntity().tickCount <= 0) {
					net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> light =
							GreedEnchants.get(player.registryAccess(), GreedEnchants.LIGHT);
					int lightLevel = light == null ? 0 : EnchantmentHelper.getEnchantmentLevel(light, player);
					hook.setDeltaMovement(hook.getDeltaMovement().multiply(1 + (0.2 * lightLevel), 1 + (0.07 * lightLevel), 1 + (0.2 * lightLevel)));
				}
			}
			
		}
	}

}
