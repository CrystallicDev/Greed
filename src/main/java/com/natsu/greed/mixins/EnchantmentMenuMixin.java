package com.natsu.greed.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.EnchantMenuHandler;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
	
	@Inject(method = "getEnchantmentList", at = @At("HEAD"), cancellable = true)
	private void onGetEnchantList(ItemStack p_39472_, int index, int cost, CallbackInfoReturnable<List<EnchantmentInstance>> ci) {
		if (!ServerConfig.USE_ENCHANTING_SYSTEM.get()) { return; }
		
		EnchantmentMenu self = (EnchantmentMenu)(Object)this;
		ContainerLevelAccess access = ((EnchantmentMenuAccessor)(Object)this).getAccess();
        Random rng = ((EnchantmentMenuAccessor)(Object)this).getRandom();
        ItemStack item = ((EnchantmentMenu)(Object)this).slots.get(0).getItem();
        //Vanilla behavior
		rng.setSeed((long) (self.getEnchantmentSeed() + index));
		List<EnchantmentInstance> vanillaList = EnchantmentHelper.selectEnchantment(rng, p_39472_, cost, false);
		if (p_39472_.is(Items.BOOK) && vanillaList.size() > 1) {
			vanillaList.remove(rng.nextInt(vanillaList.size()));
		}
		
		//Here, instead of returning, we get our modified list
		 Optional<EnchantmentTableState> c = access.evaluate((level, block) -> {
	            try {
	                Block below = level.getBlockState(block.below()).getBlock();
	                if (below == Blocks.LAPIS_BLOCK) return EnchantmentTableState.LAPIS_STATE;
	                if (below == Blocks.AMETHYST_CLUSTER) return EnchantmentTableState.AMETHYST_STATE;
	                return EnchantmentTableState.DEFAULT;
	            } catch (Exception err) {
	                return EnchantmentTableState.DEFAULT;
	            }
	        });
	        if (!c.isPresent()) return;
	        EnchantmentTableState state = c.get();
		List<EnchantmentInstance> modified = EnchantMenuHandler.onInterceptEnchant(state, rng, item, vanillaList);
		if (modified.isEmpty()) {
			//We need to change this.cost[index] to 0
			((EnchantmentMenuAccessor)(Object)this).getCosts()[index] = 0;
		}
		ci.setReturnValue(modified);
	}
	
	
	/*
	public static List<EnchantmentInstance> selectEnchantment(Random random, ItemStack itemStack, int cost, boolean idk) {
	      List<EnchantmentInstance> list = Lists.newArrayList();
	      Item item = itemStack.getItem();
	      int enchantability = itemStack.getItemEnchantability();
	      if (enchantability <= 0) {
	         return list;
	      } else {
	    	 cost += 1 + random.nextInt(enchantability / 4 + 1) + random.nextInt(enchantability / 4 + 1);
	         float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
	         cost = Mth.clamp(Math.round((float)cost + (float)cost * f), 1, Integer.MAX_VALUE);
	         List<EnchantmentInstance> list1 = EnchantmentHelper.getAvailableEnchantmentResults(cost, itemStack, idk);
	         if (!list1.isEmpty()) {
	            WeightedRandom.getRandomItem(random, list1).ifPresent(list::add);

	            while(random.nextInt(50) <= cost) {
	               if (!list.isEmpty()) {
	            	   EnchantmentHelper.filterCompatibleEnchantments(list1, Util.lastOf(list));
	               }

	               if (list1.isEmpty()) {
	                  break;
	               }

	               WeightedRandom.getRandomItem(random, list1).ifPresent(list::add);
	               cost /= 2;
	            }
	         }

	         return list;
	      }
	   }
	
	public static List<EnchantmentInstance> getAvailableEnchantmentResults(int cost, ItemStack itemStack, boolean p_44820_) {
	      List<EnchantmentInstance> list = Lists.newArrayList();
	      Item item = itemStack.getItem();
	      boolean flag = itemStack.is(Items.BOOK);

	      for(Enchantment enchantment : Registry.ENCHANTMENT) {
	         if ((!enchantment.isTreasureOnly() || p_44820_) && enchantment.isDiscoverable() && (enchantment.canApplyAtEnchantingTable(itemStack) || (flag && enchantment.isAllowedOnBooks()))) {
	            for(int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
	               if (cost >= enchantment.getMinCost(i) && cost <= enchantment.getMaxCost(i)) {
	                  list.add(new EnchantmentInstance(enchantment, i));
	                  break;
	               }
	            }
	         }
	      }

	      return list;
	   }*/

	
}