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

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
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
		// le coût affiché est recalculé après l'enchantement : on le capture avant
		int[] costs = ((EnchantmentMenuAccessor) (Object) this).getCosts();
		greed$displayedCost = (id >= 0 && id < costs.length) ? costs[id] : 0;
	}

	@Inject(method = "clickMenuButton", at = @At("RETURN"))
	private void applyLegacyXpCost(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
		if (!ServerConfig.USE_LEGACY_XP_COST.get()) return;
		if (!cir.getReturnValueZ()) return;
		if (player.getAbilities().instabuild) return;

		// vanilla a retiré (id + 1) niveaux ; on complète jusqu'au coût affiché (façon 1.7)
		int extra = greed$displayedCost - (id + 1);
		if (extra > 0) {
			player.giveExperienceLevels(-extra);
		}
	}

	// 1.20.6 : getEnchantmentList prend le FeatureFlagSet des features activées.
	@Inject(method = "getEnchantmentList", at = @At("HEAD"), cancellable = true)
	private void onGetEnchantList(FeatureFlagSet enabledFeatures, ItemStack stack, int slot, int cost,
			CallbackInfoReturnable<List<EnchantmentInstance>> ci) {
		if (!ServerConfig.USE_ENCHANTING_SYSTEM.get()) return;

		EnchantmentMenuAccessor accessor = (EnchantmentMenuAccessor) (Object) this;
		RandomSource rng = accessor.getRandom();
		ContainerLevelAccess access = accessor.getAccess();
		ItemStack item = ((EnchantmentMenu) (Object) this).slots.get(0).getItem();

		// comportement vanilla : tirage des enchants candidats
		rng.setSeed((long) (accessor.getEnchantmentSeed().get() + slot));
		List<EnchantmentInstance> vanillaList = EnchantmentHelper.selectEnchantment(enabledFeatures, rng, stack, cost, false);
		if (stack.is(Items.BOOK) && vanillaList.size() > 1) {
			vanillaList.remove(rng.nextInt(vanillaList.size()));
		}

		// état de la table (bloc dessous) puis liste modifiée par Greed
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
		List<EnchantmentInstance> modified = EnchantMenuHandler.onInterceptEnchant(c.get(), rng, item, vanillaList);
		if (modified.isEmpty()) {
			accessor.getCosts()[slot] = 0;
		}
		ci.setReturnValue(modified);
	}

}
