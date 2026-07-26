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

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber(modid = Greed.MODID, bus = EventBusSubscriber.Bus.GAME)
public class WeaponSmithTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(VillagerTradesEvent vte) {
		GreedFillingTradesEvent event = new GreedFillingTradesEvent(vte);
		if (event.getProfession() != VillagerProfession.WEAPONSMITH || !ServerConfig.getOrDefault(ServerConfig.USE_CUSTOM_WEAPON_TRADES)) return;
		
		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);
		
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_SWORD), 7, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.FLINT), 4, 3, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.SHIELD), 5, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_AXE), 9, 1, 12, 2, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.COAL, 16, 32, 1));
		
		event.addTradeTo(ProfessionLevel.APPRENTICE, new SimpleEnchantedItemForEmeralds(Items.IRON_SWORD, 12, 4, 4, Enchantments.SHARPNESS, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new SimpleEnchantedItemForEmeralds(Items.SHIELD, 12, 4, 4, Enchantments.UNBREAKING, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new SimpleEnchantedItemForEmeralds(Items.IRON_AXE, 12, 4, 4, Enchantments.SHARPNESS, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.EmeraldForItems(Items.IRON_INGOT, 8, 32, 2));
		
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_SWORD, 23, 4, 10, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_AXE, 25, 4, 10, 0.2F));
		
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_SWORD, 4, 10, 2, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new SimpleEnchantedItemForEmeralds(Items.DIAMOND_SWORD, 30, 4, 12, Enchantments.SHARPNESS, 0.2F));
		
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 38, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 40, 3, 30, 0.2F));
		
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
			ItemStack itemstack = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), i,
					trader.level().registryAccess(), Optional.empty());
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
			List<Holder<Enchantment>> enchantList = trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
					.holders().filter(h -> endItem.supportsEnchantment(h))
					.map(h -> (Holder<Enchantment>) h).collect(Collectors.toList());
			Collections.shuffle(enchantList);
			RandomSource random = RandomSource.create();
			int randomEnchantAmount = Mth.nextInt(random, this.minEnchantCount, this.maxEnchantCount);
			int emeraldCost = 0;
			List<Holder<Enchantment>> applied = new ArrayList<>();
			for (int i = 0; i < Math.min(randomEnchantAmount, enchantList.size()); i++) {
				Holder<Enchantment> enchantment = enchantList.get(i);
				for (Holder<Enchantment> appliedEnchant : applied) {
					if (!Enchantment.areCompatible(appliedEnchant, enchantment)) { continue; }
				}
				int enchantLevel = Mth.nextInt(random, enchantment.value().getMinLevel(), enchantment.value().getMaxLevel());
				if (enchantment.is(EnchantmentTags.CURSE)) {
					emeraldCost += -2 + random.nextInt(1 + enchantLevel * 5) + enchantLevel;
				} else {
					emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.is(EnchantmentTags.TREASURE) ? 7 : 5)) + 3 * enchantLevel;
				}
				emeraldCost = Math.max(emeraldCost, 1);
				endItem.enchant(enchantment, enchantLevel);
				applied.add(enchantment);
			}
			if (emeraldCost >= 64) emeraldCost = 64;
			return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class SimpleEnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final ResourceKey<Enchantment> enchant;
		private final float priceMultiplier;

		public SimpleEnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp, ResourceKey<Enchantment> enchant, float priceMultiplier) {
			this.itemStack = new ItemStack(item);
			this.baseEmeraldCost = baseEmeraldCost;
			this.maxUses = maxUses;
			this.enchant = enchant;
			this.villagerXp = villagerXp;
			this.priceMultiplier = priceMultiplier;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			ItemStack endItem = itemStack.copy();
			Holder<Enchantment> holder = trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(enchant);
			endItem.enchant(holder, 1);
			int j = Math.min(this.baseEmeraldCost, 64);
			return new MerchantOffer(new ItemCost(Items.EMERALD, j), endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
}
