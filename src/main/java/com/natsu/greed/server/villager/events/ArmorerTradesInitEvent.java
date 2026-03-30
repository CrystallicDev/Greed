package com.natsu.greed.server.villager.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent.ProfessionLevel;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorerTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(GreedFillingTradesEvent event) {
		if (event.getProfession() != VillagerProfession.ARMORER || !ServerConfig.USE_CUSTOM_ARMOR_TRADES.get()) return;
		
		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);
		
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_LEGGINGS), 7, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 4, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 5, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 9, 1, 12, 1, 0.2F));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.COAL, 16, 32, 1));
		
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_LEGGINGS), 10, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 7, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 8, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 14, 1, 12, 3, 0.2F));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new VillagerTradeHandler.EmeraldForItems(Items.IRON_INGOT, 8, 32, 1));
		
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_LEGGINGS, 17, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_BOOTS, 15, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_HELMET, 16, 3, 7, 0.2F));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new EnchantedItemForEmeralds(Items.IRON_CHESTPLATE, 20, 3, 7, 0.2F));
		
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_LEGGINGS,  3, 10, 2, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_BOOTS, 3, 7, 10, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_HELMET, 3, 7, 10, 4, 0.2F));
		event.addTradeTo(ProfessionLevel.EXPERT, new MultiEnchantedItemForEmeralds(Items.IRON_CHESTPLATE, 3, 10, 2, 4, 0.2F));
		
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 28, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 25, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 25, 3, 30, 0.2F));
		event.addTradeTo(ProfessionLevel.MASTER, new EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 30, 30, 7, 0.2F));

	}
	
	
	public static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EnchantedItemForEmeralds(Item p_35693_, int p_35694_, int p_35695_, int p_35696_) {
			this(p_35693_, p_35694_, p_35695_, p_35696_, 0.05F);
		}

		public EnchantedItemForEmeralds(Item p_35698_, int p_35699_, int p_35700_, int p_35701_, float p_35702_) {
			this.itemStack = new ItemStack(p_35698_);
			this.baseEmeraldCost = p_35699_;
			this.maxUses = p_35700_;
			this.villagerXp = p_35701_;
			this.priceMultiplier = p_35702_;
		}

		public MerchantOffer getOffer(Entity p_35704_, Random p_35705_) {
			int i = 5 + p_35705_.nextInt(15);
			ItemStack itemstack = EnchantmentHelper.enchantItem(p_35705_, new ItemStack(this.itemStack.getItem()), i,
					false);
			int j = Math.min(this.baseEmeraldCost + i, 64);
			ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
			return new MerchantOffer(itemstack1, itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
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

		public MerchantOffer getOffer(Entity p_35704_, Random p_35705_) {
			ItemStack endItem = itemStack.copy();
			List<Enchantment> enchantList = StreamSupport.stream(ForgeRegistries.ENCHANTMENTS.spliterator(), false).filter(e -> e.canEnchant(endItem))
					.collect(Collectors.toList());
			Collections.shuffle(enchantList);		// Shuffling
			Random random = new Random();
			int randomEnchantAmount = Mth.nextInt(random, this.minEnchantCount, this.maxEnchantCount);
			int emeraldCost = 0;
			List<Enchantment> applied = new ArrayList<>();
			for (int i = 0; i < Math.min(randomEnchantAmount, enchantList.size()); i++) {
				Enchantment enchantment = enchantList.get(i);
				for (Enchantment appliedEnchant : applied) { if (!appliedEnchant.isCompatibleWith(enchantment)) { continue; } }
				int enchantLevel = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.isTreasureOnly() ? 7 : 5)) + 3 * enchantLevel;
				endItem.enchant(enchantment, enchantLevel);
				applied.add(enchantment);
			}
			if (emeraldCost >= 64) { emeraldCost = 64; }
			
			ItemStack itemstack1 = new ItemStack(Items.EMERALD, emeraldCost);
			return new MerchantOffer(itemstack1, endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
}
