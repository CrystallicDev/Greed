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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.natsu.greed.common.registry.GreedCurseRegistry;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

	@Shadow
	@Final
	private ContainerLevelAccess access;
	
    @ModifyReturnValue(method = "getEnchantmentList", at = @At("RETURN"))
    private List<EnchantmentInstance> interceptEnchant(List<EnchantmentInstance> original) {
        if (original == null) return null;
        if (original.isEmpty()) return original;
        
        EnchantmentMenu menu = (EnchantmentMenu)(Object)this;
        ItemStack item = menu.slots.get(0).getItem();
        if (item == null) return original;

    	List<EnchantmentInstance> modified = new ArrayList<EnchantmentInstance>();
    	
        //This is gonna be REALLY weird
        Optional<EnchantmentTableState> c = this.access.evaluate((level, block) -> {
        	try {
        		Block below = level.getBlockState(block.below()).getBlock();
            	if (below == Blocks.LAPIS_BLOCK) return EnchantmentTableState.LAPIS_STATE;
            	if (below == Blocks.AMETHYST_CLUSTER) return EnchantmentTableState.AMETHYST_STATE;
            	return EnchantmentTableState.DEFAULT;
        	} catch (Exception err) {
        		//To avoid Block == null, etc, or weird thing if an enchant table is at Y = 0 (or -64 now) because below is null
        		return EnchantmentTableState.DEFAULT;
        	}
        });
        if (c.get() == EnchantmentTableState.AMETHYST_STATE) { return original; }		// In amethyst mode, return everything
        if (c.get() == EnchantmentTableState.DEFAULT && item.getItem() == Items.BOOK) { return modified; }		// In default mode, return null for books
        
        if (c.get() == EnchantmentTableState.DEFAULT) {
        	if (new Random().nextFloat() <= 10) {
        		Enchantment[] curses = new Enchantment[] {Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, GreedCurseRegistry.CURSE_OF_ABSORPTION.get(), 
        				GreedCurseRegistry.CURSE_OF_CREEPING.get(), GreedCurseRegistry.CURSE_OF_THE_SPONGE.get(), GreedCurseRegistry.CURSE_OF_VOIDING.get()};
        		modified.add(new EnchantmentInstance(curses[new Random().nextInt(0, curses.length)], 1));
        	} else {
            	EnchantmentInstance instance = new EnchantmentInstance(original.getFirst().enchantment, 1);
                modified.add(instance);	
        	}
        } else if (c.get() == EnchantmentTableState.LAPIS_STATE) {
        	for (EnchantmentInstance ench : original) {
        		modified.add(new EnchantmentInstance(ench.enchantment, 1));
        	}
        	if (new Random().nextFloat() <= 10) {
        		Enchantment[] curses = new Enchantment[] {Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, GreedCurseRegistry.CURSE_OF_ABSORPTION.get(), 
        				GreedCurseRegistry.CURSE_OF_CREEPING.get(), GreedCurseRegistry.CURSE_OF_THE_SPONGE.get(), GreedCurseRegistry.CURSE_OF_VOIDING.get()};
        		modified.add(new EnchantmentInstance(curses[new Random().nextInt(0, curses.length)], 1));
        	}
        }
        return modified;
    }
    
}