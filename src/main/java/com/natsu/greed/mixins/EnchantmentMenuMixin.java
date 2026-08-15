package com.natsu.greed.mixins;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.EnchantMenuHandler;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@Mixin(value = EnchantmentMenu.class, remap = false)
public abstract class EnchantmentMenuMixin {

	@Unique
	private int greed$displayedCost;

	@Inject(method = "clickMenuButton", at = @At("HEAD"))
	private void captureDisplayedCost(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
		// the shown cost gets recalculated after enchanting, so grab it beforehand
		int[] costs = ((EnchantmentMenuAccessor) (Object) this).getCosts();
		greed$displayedCost = (id >= 0 && id < costs.length) ? costs[id] : 0;
	}

	@Inject(method = "clickMenuButton", at = @At("RETURN"))
	private void applyLegacyXpCost(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
		if (!ServerConfig.USE_LEGACY_XP_COST.get()) return;
		if (!cir.getReturnValueZ()) return;
		if (player.getAbilities().instabuild) return;

		// vanilla only took (id + 1) levels, top it up to the shown cost, 1.7 style
		int extra = greed$displayedCost - (id + 1);
		if (extra > 0) {
			player.giveExperienceLevels(-extra);
		}
	}

	// getEnchantmentList takes a RegistryAccess, and selectEnchantment a Stream<Holder<Enchantment>>.
	@Inject(method = "getEnchantmentList", at = @At("HEAD"), cancellable = true)
	private void onGetEnchantList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost,
			CallbackInfoReturnable<List<EnchantmentInstance>> ci) {
		if (!ServerConfig.USE_ENCHANTING_SYSTEM.get()) return;

		EnchantmentMenuAccessor accessor = (EnchantmentMenuAccessor) (Object) this;
		RandomSource rng = accessor.getRandom();
		ContainerLevelAccess access = accessor.getAccess();
		ItemStack item = ((EnchantmentMenu) (Object) this).slots.get(0).getItem();

		// vanilla behaviour: roll the candidate enchants (tag in_enchanting_table)
		rng.setSeed((long) (accessor.getEnchantmentSeed().get() + slot));
		Optional<HolderSet.Named<Enchantment>> pool = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
				.get(EnchantmentTags.IN_ENCHANTING_TABLE);
		if (pool.isEmpty()) return;
		List<EnchantmentInstance> vanillaList = EnchantmentHelper.selectEnchantment(rng, stack, cost, pool.get().stream());
		if (stack.is(Items.BOOK) && vanillaList.size() > 1) {
			vanillaList.remove(rng.nextInt(vanillaList.size()));
		}

		// table state (block underneath), then the list Greed tweaked
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
		if (c.isEmpty()) return;
		List<EnchantmentInstance> modified = EnchantMenuHandler.onInterceptEnchant(c.get(), rng, item, vanillaList, registryAccess);
		if (modified.isEmpty()) {
			accessor.getCosts()[slot] = 0;
		}
		ci.setReturnValue(modified);
	}

}
