package com.natsu.greed.server.villager.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent.ProfessionLevel;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.village.VillagerTradesEvent;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorerTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(VillagerTradesEvent vte) {
		GreedFillingTradesEvent event = new GreedFillingTradesEvent(vte);
		if (event.getProfession() != VillagerProfession.ARMORER
				|| !ServerConfig.USE_CUSTOM_ARMOR_TRADES.get()) return;

		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);

		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_LEGGINGS), 7, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 4, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 5, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 9, 1, 12, 2, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.COAL, 16, 32, 1));

		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_LEGGINGS), 10, 1, 12, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 7, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 8, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 14, 1, 12, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.EmeraldForItems(Items.IRON_INGOT, 8, 32, 1));

		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_LEGGINGS, 25, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_BOOTS, 21, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_HELMET, 21, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_CHESTPLATE, 27, 3, 7, 0.2F));

		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_LEGGINGS, 3, 10, 2, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_BOOTS, 3, 7, 2, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_HELMET, 3, 7, 2, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_CHESTPLATE, 3, 10, 2, 3, 0.2F));

		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 40, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 35, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 35, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 45, 30, 7, 0.2F));
	}

	public static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EnchantedItemForEmeralds(Item item, int baseCost, int maxUses, int xp) {
			this(item, baseCost, maxUses, xp, 0.05F);
		}

		public EnchantedItemForEmeralds(Item item, int baseCost, int maxUses, int xp, float priceMult) {
			this.itemStack = new ItemStack(item);
			this.baseEmeraldCost = baseCost;
			this.maxUses = maxUses;
			this.villagerXp = xp;
			this.priceMultiplier = priceMult;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			int i = 5 + random.nextInt(15);
			ItemStack itemstack = EnchantmentHelper.enchantItem(trader.level().enabledFeatures(), random,
					new ItemStack(this.itemStack.getItem()), i, false);
			int j = Math.min(this.baseEmeraldCost + i, 64);
			return new MerchantOffer(new ItemCost(Items.EMERALD, j), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class MultiEnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;
		private final int minEnchantCount;
		private final int maxEnchantCount;

		public MultiEnchantedItemForEmeralds(Item item, int maxUses, int xp, int min, int max, float priceMult) {
			this.itemStack = new ItemStack(item);
			this.maxUses = maxUses;
			this.villagerXp = xp;
			this.minEnchantCount = min;
			this.maxEnchantCount = max;
			this.priceMultiplier = priceMult;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource rng) {
			ItemStack endItem = itemStack.copy();
			List<Enchantment> enchantList = BuiltInRegistries.ENCHANTMENT.stream()
					.filter(e -> e.canEnchant(endItem)).collect(Collectors.toList());
			Collections.shuffle(enchantList);
			RandomSource random = RandomSource.create();
			int randomEnchantAmount = Mth.nextInt(random, this.minEnchantCount, this.maxEnchantCount);
			int emeraldCost = 0;
			List<Enchantment> applied = new ArrayList<>();
			for (int i = 0; i < Math.min(randomEnchantAmount, enchantList.size()); i++) {
				Enchantment enchantment = enchantList.get(i);
				for (Enchantment appliedEnchant : applied) {
					if (!appliedEnchant.isCompatibleWith(enchantment)) { continue; }
				}
				int enchantLevel = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				if (enchantment.isCurse()) {
					emeraldCost += -2 + random.nextInt(1 + enchantLevel * 5) + enchantLevel;
				} else {
					emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.isTreasureOnly() ? 7 : 5)) + 3 * enchantLevel;
				}
				emeraldCost = Math.max(emeraldCost, 1);
				endItem.enchant(enchantment, enchantLevel);
				applied.add(enchantment);
			}
			if (emeraldCost >= 64) emeraldCost = 64;
			return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
}
