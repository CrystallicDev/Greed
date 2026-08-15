package com.natsu.greed.server.villager.events;

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
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.village.VillagerTradesEvent;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LibrarianTradesInitEvent {

	// original rarity weights: COMMON=10, UNCOMMON=5, RARE=2, VERY_RARE=1.
	// rarity is gone now, so common||uncommon roughly means weight >= 5.
	private static final int COMMON_UNCOMMON_WEIGHT = 5;

	@SubscribeEvent
	public static void onTradeSetup(VillagerTradesEvent vte) {
		GreedFillingTradesEvent event = new GreedFillingTradesEvent(vte);
		if (event.getProfession() != VillagerProfession.LIBRARIAN
				|| !ServerConfig.USE_CUSTOM_BOOK_TRADES.get()) return;

		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);

		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(Items.NAME_TAG, 7, 1, 12, 3));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.PAPER, 24, 16, 2));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1));

		event.addTradeTo(ProfessionLevel.APPRENTICE, new SimpleEnchantBookForEmeralds(1));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(Items.ENCHANTING_TABLE, 20, 1, 1, 7));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(Items.LANTERN, 1, 1, 5));

		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new VillagerTradeHandler.EmeraldForItems(Items.LAPIS_LAZULI, 5, 12, 10));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantBookForEmeralds(5));

		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantBookForEmeralds(10, 1, 3));
		event.addTradeTo(ProfessionLevel.EXPERT, new VillagerTradeHandler.EmeraldForItems(Items.WRITABLE_BOOK, 2, 12, 30));
		event.addTradeTo(ProfessionLevel.EXPERT, new VillagerTradeHandler.ItemsForEmeralds(Items.CLOCK, 5, 1, 15));

		event.addTradeTo(ProfessionLevel.MASTER, new MultiEnchantBookForEmeralds(15, 2, 5));
	}

	// tradeable enchants (isTradeable) from the registry
	private static List<Enchantment> tradeable(Entity trader) {
		return BuiltInRegistries.ENCHANTMENT.stream()
				.filter(Enchantment::isTradeable)
				.collect(Collectors.toList());
	}

	static class SimpleEnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;

		public SimpleEnchantBookForEmeralds(int xp) {
			this.villagerXp = xp;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			List<Enchantment> list = tradeable(trader).stream()
					.filter(e -> e.getWeight() >= COMMON_UNCOMMON_WEIGHT)
					.collect(Collectors.toList());
			if (list.isEmpty()) return null;
			Enchantment enchantment = list.get(random.nextInt(list.size()));
			int i = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
			int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
			if (enchantment.isTreasureOnly()) j *= 2;
			if (j > 64) j = 64;
			return new MerchantOffer(new ItemCost(Items.EMERALD, j), Optional.of(new ItemCost(Items.BOOK)),
					itemstack, 12, this.villagerXp, 0.2F);
		}
	}

	static class EnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;

		public EnchantBookForEmeralds(int xp) {
			this.villagerXp = xp;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			List<Enchantment> list = tradeable(trader);
			if (list.isEmpty()) return null;
			Enchantment enchantment = list.get(random.nextInt(list.size()));
			int i = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
			int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
			if (enchantment.isTreasureOnly()) j *= 2;
			if (j > 64) j = 64;
			return new MerchantOffer(new ItemCost(Items.EMERALD, j), Optional.of(new ItemCost(Items.BOOK)),
					itemstack, 12, this.villagerXp, 0.2F);
		}
	}

	static class MultiEnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;
		private final int minEnchant;
		private final int maxEnchant;

		public MultiEnchantBookForEmeralds(int xp, int minEnchantCount, int maxEnchantCount) {
			this.villagerXp = xp;
			this.minEnchant = minEnchantCount;
			this.maxEnchant = maxEnchantCount;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			List<Enchantment> enchantList = tradeable(trader);
			if (enchantList.isEmpty()) return null;
			Collections.shuffle(enchantList);
			int randomEnchantAmount = Mth.nextInt(random, this.minEnchant, this.maxEnchant);
			int emeraldCost = 0;
			ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
			for (int i = 0; i < Math.min(randomEnchantAmount, enchantList.size()); i++) {
				Enchantment enchantment = enchantList.get(i);
				int enchantLevel = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				if (enchantment.isCurse()) {
					emeraldCost += -2 + random.nextInt(1 + enchantLevel * 5) + enchantLevel;
				} else {
					emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.isTreasureOnly() ? 7 : 5)) + 3 * enchantLevel;
				}
				emeraldCost = Math.max(emeraldCost, 1);
				itemStack.enchant(enchantment, enchantLevel);
			}
			if (emeraldCost >= 64) emeraldCost = 64;
			return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), Optional.of(new ItemCost(Items.BOOK)),
					itemStack, 12, this.villagerXp, 0.2F);
		}
	}

}
