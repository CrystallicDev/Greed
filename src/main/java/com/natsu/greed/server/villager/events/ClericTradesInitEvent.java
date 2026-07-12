package com.natsu.greed.server.villager.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mojang.datafixers.util.Pair;
import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent.ProfessionLevel;
import com.natsu.greed.utils.PotionCreatorUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClericTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(VillagerTradesEvent vte) {
		GreedFillingTradesEvent event = new GreedFillingTradesEvent(vte);
		if (event.getProfession() != VillagerProfession.CLERIC || !ServerConfig.USE_CUSTOM_MAP_TRADES.get()) return;
		
		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);
		
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.ROTTEN_FLESH, 32, 16, 2));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.SPIDER_EYE, 8, 16, 2));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.BONE, 12, 16, 2));
		
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(Items.GLASS_BOTTLE, 1, 1, 32, 2));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.EmeraldForItems(Blocks.GLOWSTONE, 4, 12, 4));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new PotionForEmeralds(12, List.of(new MobEffectInstance(MobEffects.HEAL)), "item.greed.trade.heal", 10, 15));

		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new VillagerTradeHandler.ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 2, 1, 30, 5));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new RandomPotionForEmeralds(28, 1, 3*20*60, 0, "item.greed.trade.1_random_potion", 10, 3));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new RandomSplashPotionForEmeralds(28, 1, 3*20*60, 0, "item.greed.trade.1_random_splash_potion", 10, 3));

		event.addTradeTo(ProfessionLevel.EXPERT, new VillagerTradeHandler.ItemsForEmeralds(Items.GOLDEN_APPLE, 12, 1, 16, 10));
		event.addTradeTo(ProfessionLevel.EXPERT, new SplashPotionForEmeralds(18, List.of(new MobEffectInstance(MobEffects.WEAKNESS, 30*20)), "item.greed.trade.splash_weakness", 10, 15));

		event.addTradeTo(ProfessionLevel.MASTER, new RandomPotionForEmeralds(28, 2, 3*20*60, 0, "item.greed.trade.2_random_potion", 10, 3));
		event.addTradeTo(ProfessionLevel.MASTER, new RandomSplashPotionForEmeralds(28, 2, 3*20*60, 0, "item.greed.trade.2_random_splash_potion", 10, 3));
		event.addTradeTo(ProfessionLevel.MASTER, new VillagerTradeHandler.ItemsForEmeralds(Items.ENCHANTED_GOLDEN_APPLE, 48, 1, 8, 1));
	}
	
	public static class PotionForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final List<MobEffectInstance> effects;
		private final String displayName;
		private final int maxUses;
		private final int villagerXp;

		public PotionForEmeralds(int emeraldCost, List<MobEffectInstance> effect, String displayName,
				int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.effects = effect;
			this.displayName = displayName;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			ItemStack potion = PotionCreatorUtils.makeIntoPotion(Items.POTION, effects);
			potion.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.GLASS_BOTTLE), potion,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class RandomPotionForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final String displayName;
		private final int effectCount;
		private final int duration;
		private final int amplifier;
		private final int maxUses;
		private final int villagerXp;

		public RandomPotionForEmeralds(int emeraldCost, int effectCount, int duration, int amplifier, String displayName,
				int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.effectCount = effectCount;
			this.duration = duration;
			this.amplifier = amplifier;
			this.displayName = displayName;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			List<MobEffect> possibleEffects = StreamSupport.stream(ForgeRegistries.MOB_EFFECTS.spliterator(), false).collect(Collectors.toList());
			List<MobEffectInstance> effects = new ArrayList<>();
			Collections.shuffle(possibleEffects);
			for (int i = 0; i < Math.min(this.effectCount, possibleEffects.size()); i++) {
				effects.add(new MobEffectInstance(possibleEffects.get(i), this.duration, this.amplifier));
			}
			

			ItemStack potion = PotionCreatorUtils.makeIntoPotion(Items.POTION, effects);
			potion.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.GLASS_BOTTLE), potion,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class SplashPotionForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final List<MobEffectInstance> effects;
		private final String displayName;
		private final int maxUses;
		private final int villagerXp;

		public SplashPotionForEmeralds(int emeraldCost, List<MobEffectInstance> effect, String displayName,
				int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.effects = effect;
			this.displayName = displayName;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			ItemStack potion = PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, effects);
			potion.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.GLASS_BOTTLE), potion,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class RandomSplashPotionForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final String displayName;
		private final int effectCount;
		private final int duration;
		private final int amplifier;
		private final int maxUses;
		private final int villagerXp;

		public RandomSplashPotionForEmeralds(int emeraldCost, int effectCount, int duration, int amplifier, String displayName,
				int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.effectCount = effectCount;
			this.duration = duration;
			this.amplifier = amplifier;
			this.displayName = displayName;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			List<MobEffect> possibleEffects = StreamSupport.stream(ForgeRegistries.MOB_EFFECTS.spliterator(), false).collect(Collectors.toList());
			List<MobEffectInstance> effects = new ArrayList<>();
			Collections.shuffle(possibleEffects);
			for (int i = 0; i < Math.min(this.effectCount, possibleEffects.size()); i++) {
				effects.add(new MobEffectInstance(possibleEffects.get(i), this.duration, this.amplifier));
			}
			ItemStack potion = PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, effects);
			potion.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.GLASS_BOTTLE), potion,
					maxUses, villagerXp, 0.2f);
		}
	}
}
