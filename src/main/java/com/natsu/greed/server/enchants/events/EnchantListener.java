package com.natsu.greed.server.enchants.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.TickTask;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Greed.MODID)
public class EnchantListener {

	// Réduire la durée de l'effet re-déclenche MobEffectEvent.Added : on ignore l'instance qu'on repose.
	private static final List<MobEffectInstance> instancesToSkip = new ArrayList<>();

	private static int levelOf(LivingEntity entity, Enchantment enchant, ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(enchant, stack);
	}

	@SubscribeEvent
	public static void onPickupXP(PlayerXpEvent.PickupXp event) {
		Player player = event.getEntity();
		for (ItemStack armor : player.getArmorSlots()) {
			if (levelOf(player, GreedEnchants.CURSE_OF_ABSORPTION.get(), armor) > 0) {
				event.getOrb().value *= 0.75;
				return;
			}
		}
	}

	@SubscribeEvent
	public static void onPotionApply(MobEffectEvent.Added event) {
		if (!(event.getEntity() instanceof Player player)) return;
		if (instancesToSkip.remove(event.getEffectInstance())) return;

		boolean hasCurse = false;
		for (ItemStack armor : player.getArmorSlots()) {
			if (levelOf(player, GreedEnchants.CURSE_OF_THE_SPONGE.get(), armor) > 0) {
				hasCurse = true;
			}
		}
		if (!hasCurse) return;

		MobEffectInstance original = event.getEffectInstance();
		if (original.getEffect().value().isBeneficial()) {
			int newDura = Math.max(1, (int) Math.round(original.getDuration() * (new Random().nextFloat(0.6f, 0.8f))));
			player.level().getServer().tell(new TickTask(0, () -> {
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
		if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
			ItemStack boots = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
			if (boots.isEmpty()) { return; }
			if (levelOf(event.getEntity(), GreedEnchants.CURSE_OF_HEAVYWEIGHT.get(), boots) > 0) {
				event.setAmount(event.getAmount() * 2);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
		if (levelOf(player, GreedEnchants.CURSE_OF_CREEPING.get(), boots) > 0) {
			if (player.isCrouching() && player.onGround()) {
				player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
			}
		}
	}

	@SubscribeEvent
	public static void onEnchantLevelSet(EnchantmentLevelSetEvent event) {
		if (!ServerConfig.USE_ENCHANTING_SYSTEM.get()) { return; }
		Block below = event.getLevel().getBlockState(event.getPos().below()).getBlock();
		EnchantmentTableState state;
		if (below == Blocks.LAPIS_BLOCK) state = EnchantmentTableState.LAPIS_STATE;
		else if (below == Blocks.AMETHYST_CLUSTER) state = EnchantmentTableState.AMETHYST_STATE;
		else state = EnchantmentTableState.DEFAULT;

		if (event.getItem().getItem() == Items.BOOK && state == EnchantmentTableState.DEFAULT) {
			event.setEnchantLevel(0);
		}
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		if (hasCurse(event.getPlayer(), event.getLeft()) || hasCurse(event.getPlayer(), event.getRight())) {
			event.setOutput(ItemStack.EMPTY);
			event.setCost(40);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onArrow(ArrowLooseEvent event) {
		Player player = event.getEntity();
		int level = EnchantmentHelper.getEnchantmentLevel(GreedEnchants.STRETCHED.get(), player);
		if (level > 0) {
			float newCharge = event.getCharge() * (1f + (0.5f * level));
			event.setCharge((int) Math.round(newCharge));
		}
	}

	private static boolean hasCurse(Player player, ItemStack stack) {
		return levelOf(player, GreedEnchants.CURSE_OF_COMBINATION.get(), stack) > 0;
	}

}
