package com.natsu.greed.server.enchants.events;

import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.natsu.greed.common.registry.GreedCurseRegistry;

import net.minecraft.core.BlockPos;
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
	        if (EnchantmentHelper.getItemEnchantmentLevel(GreedCurseRegistry.CURSE_OF_ABSORPTION.get(), armor) > 0) {
	            event.getOrb().value -= 1;
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
	        if (EnchantmentHelper.getItemEnchantmentLevel(GreedCurseRegistry.CURSE_OF_THE_SPONGE.get(), armor) > 0) {
	            hasCurse = true;
	        }
	    }

	    if (!hasCurse) return;

	    MobEffectInstance effect = event.getPotionEffect();

	    if (effect.getEffect().isBeneficial()) {
	        effect = new MobEffectInstance(
	                effect.getEffect(),
	                (int)Math.round(new Random().nextFloat(0.6f, 0.8f) * effect.getDuration()),
	                effect.getAmplifier()
	        );
	        event.setCanceled(true);
	        event.getEntityLiving().addEffect(effect);
	    }
	}
	
	@SubscribeEvent
	public static void onBlockDrops(BlockEvent.BreakEvent event) {

	    Player player = event.getPlayer();
	    if (player == null) return;

	    ItemStack tool = player.getMainHandItem();

	    if (EnchantmentHelper.getItemEnchantmentLevel(GreedCurseRegistry.CURSE_OF_VOIDING.get(), tool) > 0) {

	    	LevelAccessor level = event.getWorld();
	    	BlockPos pos = event.getPos();
	        List<ItemStack> drops = Block.getDrops(level.getBlockState(pos), (ServerLevel)level, pos, level.getBlockEntity(pos), player, tool);
	        drops.removeIf(stack -> player.getRandom().nextFloat() < 0.5f);
	    }
	}
	
	@SubscribeEvent
	public static void onPlayerTick(LivingEvent.LivingUpdateEvent event) {

	    if (!(event.getEntity() instanceof Player player))
	        return;

	    ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

	    if (EnchantmentHelper.getItemEnchantmentLevel(GreedCurseRegistry.CURSE_OF_CREEPING.get(), boots) > 0) {

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
	    }
	}
	
	public static boolean hasCurse(ItemStack stack) {
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		for (Enchantment enchant : enchants.keySet()) {
			if (enchant == GreedCurseRegistry.CURSE_OF_COMBINATION.get()) {
				return true;
			}
		}
		return false;
	}
	
}
