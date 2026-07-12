package com.natsu.greed.server.fishing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.NoteBlockEvent.Play;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class FishingEventListener {
	
	//This should be compatible with Pride's Fishing Hook handling ?
	@SubscribeEvent
	public static void onFishingHook(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof FishingHook hook) {
			if (hook.getOwner() instanceof Player player) {
				if (!event.getLevel().isClientSide && event.getEntity().tickCount <= 0) {
					int lightLevel = EnchantmentHelper.getEnchantmentLevel(GreedEnchants.LIGHT.get(), player);
					hook.setDeltaMovement(hook.getDeltaMovement().multiply(1 + (0.2 * lightLevel), 1 + (0.07 * lightLevel), 1 + (0.2 * lightLevel)));
				}
			}
			
		}
	}

}
