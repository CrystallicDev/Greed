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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponSmithTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(GreedFillingTradesEvent event) {
		if (event.getProfession() != VillagerProfession.WEAPONSMITH || !ServerConfig.USE_CUSTOM_WEAPON_TRADES.get()) return;
		
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

		public MerchantOffer getOffer(Entity p_35704_, net.minecraft.util.RandomSource p_35705_) {
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

		public MerchantOffer getOffer(Entity p_35704_, net.minecraft.util.RandomSource p_35705_) {
			ItemStack endItem = itemStack.copy();
			List<Enchantment> enchantList = StreamSupport.stream(ForgeRegistries.ENCHANTMENTS.spliterator(), false).filter(e -> e.canEnchant(endItem))
					.collect(Collectors.toList());
			Collections.shuffle(enchantList);		// Shuffling
			net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create();
			int randomEnchantAmount = Mth.nextInt(random, this.minEnchantCount, this.maxEnchantCount);
			int emeraldCost = 0;
			List<Enchantment> applied = new ArrayList<>();
			for (int i = 0; i < Math.min(randomEnchantAmount, enchantList.size()); i++) {
				Enchantment enchantment = enchantList.get(i);
				for (Enchantment appliedEnchant : applied) { if (!appliedEnchant.isCompatibleWith(enchantment)) { continue; } }
				int enchantLevel = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				if (enchantment.isCurse()) {
					emeraldCost += -2 + random.nextInt(1 + enchantLevel * 5) + enchantLevel;
				} else { emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.isTreasureOnly() ? 7 : 5)) + 3 * enchantLevel; }
				emeraldCost = Math.max(emeraldCost, 1);
				endItem.enchant(enchantment, enchantLevel);
				applied.add(enchantment);
			}
			if (emeraldCost >= 64) { emeraldCost = 64; }
			
			ItemStack itemstack1 = new ItemStack(Items.EMERALD, emeraldCost);
			return new MerchantOffer(itemstack1, endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
	
	public static class SimpleEnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final Enchantment enchant;
		private final float priceMultiplier;


		public SimpleEnchantedItemForEmeralds(Item p_35698_, int baseEmeraldCost, int maxUses, int villagerXp, Enchantment enchant, float priceMultiplier) {
			this.itemStack = new ItemStack(p_35698_);
			this.baseEmeraldCost = baseEmeraldCost;
			this.maxUses = maxUses;
			this.enchant = enchant;
			this.villagerXp = villagerXp;
			this.priceMultiplier = priceMultiplier;
		}

		public MerchantOffer getOffer(Entity p_35704_, net.minecraft.util.RandomSource p_35705_) {
			ItemStack endItem = itemStack.copy();
			endItem.enchant(enchant, 1);
			int j = Math.min(this.baseEmeraldCost, 64);
			ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
			return new MerchantOffer(itemstack1, endItem, this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
}
