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
	
    @ModifyReturnValue(
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
        if (state == EnchantmentTableState.AMETHYST_STATE) return original;

        List<EnchantmentInstance> modified = new ArrayList<>();

        Enchantment[] curses = {
            Enchantments.BINDING_CURSE,
            Enchantments.VANISHING_CURSE,
            GreedEnchants.CURSE_OF_ABSORPTION.get(),
            GreedEnchants.CURSE_OF_CREEPING.get(),
            GreedEnchants.CURSE_OF_THE_SPONGE.get(),
            GreedEnchants.CURSE_OF_VOIDING.get(),
            GreedEnchants.CURSE_OF_SCARCITY.get(),
            GreedEnchants.CURSE_OF_HEAVYWEIGHT.get()
        };

        if (state == EnchantmentTableState.DEFAULT) {
            if (item.getItem() == Items.BOOK) return new ArrayList<>();
            if (rng.nextFloat() <= 0.10f) {
                modified.add(new EnchantmentInstance(curses[rng.nextInt(curses.length)], 1));
            } else {
                modified.add(new EnchantmentInstance(original.get(0).enchantment, 1));
            }

        } else if (state == EnchantmentTableState.LAPIS_STATE) {
            for (EnchantmentInstance ench : original) {
                modified.add(new EnchantmentInstance(ench.enchantment, 1));
            }
            if (rng.nextFloat() <= 0.10f) {
                modified.add(new EnchantmentInstance(curses[rng.nextInt(curses.length)], 1));
            }
        }

        return modified;
    }
}