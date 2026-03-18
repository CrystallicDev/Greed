package com.natsu.greed.server.enchants.events;

import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EnchantListener {

	@SubscribeEvent
	public static void onPickupXP(PlayerXpEvent.PickupXp event) {
	    Player player = event.getPlayer();

	    for (ItemStack armor : player.getArmorSlots()) {
	        if (EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_ABSORPTION.get(), armor) > 0) {
	            event.getOrb().value *= 0.75;
	            return;
	        }
	    }
	}
	
	@SubscribeEvent
	public static void onPotionApply(PotionEvent.PotionAddedEvent event) {

	    if (!(event.getEntity() instanceof Player player))
	        return;

	    boolean hasCurse = false;

	    for (ItemStack armor : player.getArmorSlots()) {
	        if (EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_THE_SPONGE.get(), armor) > 0) {
	            hasCurse = true;
	        }
	    }

	    if (!hasCurse) return;

	    MobEffectInstance original = event.getPotionEffect();

	    if (original.getEffect().isBeneficial()) {
	        int newDura = Math.max(1, (int)Math.round(original.getDuration() * (new Random().nextFloat(0.6f, 0.8f))));
	        player.getLevel().getServer().tell(new TickTask(0, 
	        		() -> {
	        			player.removeEffect(original.getEffect());
	        			player.addEffect(new MobEffectInstance(original.getEffect(), newDura, original.getAmplifier(),
	        					original.isAmbient(), original.isVisible(), original.showIcon()));
	        			
	        		}));
	    }
	}
	
	@SubscribeEvent
	public static void onPlayerTick(LivingEvent.LivingUpdateEvent event) {

	    if (!(event.getEntity() instanceof Player player))
	        return;

	    ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

	    if (EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_CREEPING.get(), boots) > 0) {

	        if (player.isCrouching() && player.isOnGround()) {
	            player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
	        }
	    }
	}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
	    ItemStack left = event.getLeft();
	    ItemStack right = event.getRight();
	    if (hasCurse(left) || hasCurse(right)) {
	        event.setOutput(ItemStack.EMPTY);
	        event.setCost(40); 
	        if (event.isCancelable()) { event.setCanceled(true); }
	    }
	}
	
	public static boolean hasCurse(ItemStack stack) {
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		for (Enchantment enchant : enchants.keySet()) {
			if (enchant == GreedEnchants.CURSE_OF_COMBINATION.get()) {
				return true;
			}
		}
		return false;
	}
	
}
