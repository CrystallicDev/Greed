package com.natsu.greed.server.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.natsu.greed.config.ServerConfig;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EnchantMenuHandler {

	public static List<EnchantmentInstance> onInterceptEnchant(EnchantmentTableState state, Random rng, ItemStack item, List<EnchantmentInstance> original){
        if (state == EnchantmentTableState.DEFAULT && item.getItem() == Items.BOOK) return new ArrayList<>();

        List<EnchantmentInstance> modified = new ArrayList<>();
        Enchantment[] curses = ServerConfig.getCurseList(state, item).toArray(new Enchantment[0]);
        List<EnchantmentInstance> allowedEnchants = filterEnchants(state, original);
        if (allowedEnchants.isEmpty()) { return allowedEnchants; }
        
        if (state == EnchantmentTableState.DEFAULT) {
            if (rng.nextFloat() <= ServerConfig.getCurseProbability(state)) {
                modified.add(new EnchantmentInstance(curses[rng.nextInt(curses.length)], 1));
            } else {
                modified.add(new EnchantmentInstance(allowedEnchants.get(0).enchantment, 1));
            }

        } else if (state == EnchantmentTableState.LAPIS_STATE) {
            for (EnchantmentInstance ench : allowedEnchants) {
                modified.add(new EnchantmentInstance(ench.enchantment, 1));
            }
            if (rng.nextFloat() <= ServerConfig.getCurseProbability(state)) {
                modified.add(new EnchantmentInstance(curses[rng.nextInt(curses.length)], 1));
            }
        } else if (state == EnchantmentTableState.AMETHYST_STATE) {
            modified.addAll(allowedEnchants);
        	if (rng.nextFloat() <= ServerConfig.getCurseProbability(state)) {
                modified.add(new EnchantmentInstance(curses[rng.nextInt(curses.length)], 1));
            }
        }

        return modified;
	}
	
	private static List<EnchantmentInstance> filterEnchants(EnchantmentTableState state, List<EnchantmentInstance> original) {
		List<EnchantmentInstance> allowedEnchants = new ArrayList<>();
		boolean isWhiteList = ServerConfig.isWhiteList(state);
		List<Enchantment> stageEnchants = ServerConfig.getEnchantmentList(state);
		if (isWhiteList) {
			for (EnchantmentInstance instance : original) {
				if (stageEnchants.contains(instance.enchantment)) {
					allowedEnchants.add(instance);
				}
			}
		} else {
			for (EnchantmentInstance instance : original) {
				if (!stageEnchants.contains(instance.enchantment)) {
					allowedEnchants.add(instance);
				}
			}
		}
		return allowedEnchants;
	}
	
}
