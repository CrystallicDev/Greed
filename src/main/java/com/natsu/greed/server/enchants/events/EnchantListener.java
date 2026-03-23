package com.natsu.greed.server.enchants.events;

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class EnchantListener {

	// Our issue is, that if we reduce the PotionAddedEvent's mob instance duration, it re triggers, creating a call loop
	private static List<MobEffectInstance> instancesToSkip = new ArrayList<>();
	
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
	    if (instancesToSkip.contains(event.getPotionEffect())) {
	    	instancesToSkip.remove(event.getPotionEffect());
	    	return;
	    }

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
	        			MobEffectInstance instance = new MobEffectInstance(original.getEffect(), newDura, original.getAmplifier(),
	        					original.isAmbient(), original.isVisible(), original.showIcon());
	        			instancesToSkip.add(instance);
	        			player.addEffect(instance);
	        			
	        		}));
	    }
	}
	
	@SubscribeEvent
	public static void onDamage(LivingHurtEvent event) {
		if (event.getSource().isFall()) {
			ItemStack boots = event.getEntityLiving().getItemBySlot(EquipmentSlot.FEET);
			if (boots != null) { return; }
		    if (EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_HEAVYWEIGHT.get(), boots) > 0) {
		    	event.setAmount(event.getAmount() * 2);
		    }
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
	public static void onEnchantLevelSet(EnchantmentLevelSetEvent event) {
		Block below = event.getWorld().getBlockState(event.getPos().below()).getBlock();
		EnchantmentTableState state;
		if (below == Blocks.LAPIS_BLOCK) state = EnchantmentTableState.LAPIS_STATE;
		else if (below == Blocks.AMETHYST_CLUSTER) state = EnchantmentTableState.AMETHYST_STATE;
		else state = EnchantmentTableState.DEFAULT;
        
		if (event.getItem().getItem() == Items.BOOK && state == EnchantmentTableState.DEFAULT) {
			event.setLevel(0);
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
