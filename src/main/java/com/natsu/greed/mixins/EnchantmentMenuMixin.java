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
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.EnchantMenuHandler;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
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
	
    /*@ModifyReturnValue(
        method = "getEnchantmentList",
        at = @At("RETURN")
    )
    private List<EnchantmentInstance> interceptEnchant(List<EnchantmentInstance> original) {
        if (original == null || original.isEmpty()) return original;

        ContainerLevelAccess access = ((EnchantmentMenuAccessor)(Object)this).getAccess();
        Random rng = ((EnchantmentMenuAccessor)(Object)this).getRandom();
        
        ItemStack item = ((EnchantmentMenu)(Object)this).slots.get(0).getItem();
        if (item.isEmpty()) return original;

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
        if (!c.isPresent()) return original;
        EnchantmentTableState state = c.get();
        
        return EnchantMenuHandler.onInterceptEnchant(state, rng, item, original);
    }*/

	
}