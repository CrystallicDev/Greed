package com.natsu.greed.server.villager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/**
 * public copies of vanilla's private ItemListings.
 * MerchantOffer takes ItemCost, and dye/potion/stew go through DataComponents.
 * the enchant trades (Librarian/Armorer/ToolSmith/WeaponSmith) are handled separately.
 */
public class VillagerTradeHandler {

	public static class DyedArmorForEmeralds implements VillagerTrades.ItemListing {
		private final Item item;
		private final int value;
		private final int maxUses;
		private final int villagerXp;

		public DyedArmorForEmeralds(Item item, int value) {
			this(item, value, 12, 1);
		}

		public DyedArmorForEmeralds(Item item, int value, int maxUses, int villagerXp) {
			this.item = item;
			this.value = value;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			ItemCost cost = new ItemCost(Items.EMERALD, this.value);
			ItemStack result = new ItemStack(this.item);
			if (result.is(ItemTags.DYEABLE)) {
				List<DyeItem> list = Lists.newArrayList();
				list.add(getRandomDye(random));
				if (random.nextFloat() > 0.7F) {
					list.add(getRandomDye(random));
				}
				if (random.nextFloat() > 0.8F) {
					list.add(getRandomDye(random));
				}
				result = DyedItemColor.applyDyes(result, list);
			}
			return new MerchantOffer(cost, result, this.maxUses, this.villagerXp, 0.2F);
		}

		private static DyeItem getRandomDye(RandomSource random) {
			return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
		}
	}

	public static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final ItemCost cost;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EmeraldForItems(ItemLike item, int cost, int maxUses, int villagerXp) {
			this.cost = new ItemCost(item.asItem(), cost);
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
			this.priceMultiplier = 0.05F;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			return new MerchantOffer(this.cost, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
		private final Map<VillagerType, Item> trades;
		private final int cost;
		private final int maxUses;
		private final int villagerXp;

		public EmeraldsForVillagerTypeItem(int cost, int maxUses, int villagerXp, Map<VillagerType, Item> trades) {
			this.trades = trades;
			this.cost = cost;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			if (trader instanceof VillagerDataHolder holder) {
				Item item = this.trades.get(holder.getVillagerData().getType());
				if (item == null) {
					return null;
				}
				return new MerchantOffer(new ItemCost(item, this.cost), new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
			}
			return null;
		}
	}

	public static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemCost fromItem;
		private final int emeraldCost;
		private final ItemStack toItem;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, Item toItem, int toCount, int maxUses, int villagerXp) {
			this(fromItem, fromCount, emeraldCost, toItem, toCount, maxUses, villagerXp, 0.05F);
		}

		public ItemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, Item toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
			this.fromItem = new ItemCost(fromItem, fromCount);
			this.emeraldCost = emeraldCost;
			this.toItem = new ItemStack(toItem, toCount);
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
			this.priceMultiplier = priceMultiplier;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost), Optional.of(this.fromItem),
					this.toItem.copy(), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsForEmeralds(Block item, int emeraldCost, int numberOfItems, int maxTrades, int villagerXp) {
			this(new ItemStack(item), emeraldCost, numberOfItems, maxTrades, villagerXp);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int villagerXp) {
			this(new ItemStack(item), emeraldCost, numberOfItems, 12, villagerXp);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxTrades, int villagerXp) {
			this(new ItemStack(item), emeraldCost, numberOfItems, maxTrades, villagerXp);
		}

		public ItemsForEmeralds(ItemStack item, int emeraldCost, int numberOfItems, int maxTrades, int villagerXp) {
			this(item, emeraldCost, numberOfItems, maxTrades, villagerXp, 0.05F);
		}

		public ItemsForEmeralds(ItemStack item, int emeraldCost, int numberOfItems, int maxTrades, int villagerXp, float priceMultiplier) {
			this.itemStack = item;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxTrades;
			this.villagerXp = villagerXp;
			this.priceMultiplier = priceMultiplier;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost),
					new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class SuspiciousStewForEmerald implements VillagerTrades.ItemListing {
		final SuspiciousStewEffects effects;
		final int xp;
		private final float priceMultiplier;

		public SuspiciousStewForEmerald(Holder<MobEffect> effect, int duration, int xp) {
			this.effects = new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(effect, duration)));
			this.xp = xp;
			this.priceMultiplier = 0.05F;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			ItemStack itemstack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
			itemstack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.effects);
			return new MerchantOffer(new ItemCost(Items.EMERALD), itemstack, 12, this.xp, this.priceMultiplier);
		}
	}

	public static class TippedArrowForItemsAndEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack toItem;
		private final int toCount;
		private final int emeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final Item fromItem;
		private final int fromCount;
		private final float priceMultiplier;

		public TippedArrowForItemsAndEmeralds(Item fromItem, int fromCount, Item toItem, int toCount, int emeraldCost, int maxUses, int villagerXp) {
			this.toItem = new ItemStack(toItem);
			this.emeraldCost = emeraldCost;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
			this.fromItem = fromItem;
			this.fromCount = fromCount;
			this.toCount = toCount;
			this.priceMultiplier = 0.05F;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			ItemCost cost = new ItemCost(Items.EMERALD, this.emeraldCost);
			List<Holder<Potion>> list = BuiltInRegistries.POTION.holders()
					.filter(h -> !h.value().getEffects().isEmpty() && trader.level().potionBrewing().isBrewablePotion(h))
					.collect(Collectors.toList());
			Holder<Potion> holder = Util.getRandom(list, random);
			ItemStack itemstack = new ItemStack(this.toItem.getItem(), this.toCount);
			itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(holder));
			return new MerchantOffer(cost, Optional.of(new ItemCost(this.fromItem, this.fromCount)), itemstack,
					this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<Structure> destination;
		private final String displayName;
		private final Holder<MapDecorationType> destinationType;
		private final int maxUses;
		private final int villagerXp;

		public TreasureMapForEmeralds(int emeraldCost, TagKey<Structure> destination, String displayName,
				Holder<MapDecorationType> destinationType, int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.destination = destination;
			this.displayName = displayName;
			this.destinationType = destinationType;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
			if (!(trader.level() instanceof ServerLevel serverlevel)) {
				return null;
			}
			BlockPos blockpos = serverlevel.findNearestMapStructure(this.destination, trader.blockPosition(), 100, true);
			if (blockpos == null) {
				return null;
			}
			ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) 2, true, true);
			MapItem.renderBiomePreviewMap(serverlevel, itemstack);
			MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
			itemstack.set(DataComponents.ITEM_NAME, Component.translatable(this.displayName));
			return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost), Optional.of(new ItemCost(Items.COMPASS)),
					itemstack, this.maxUses, this.villagerXp, 0.2F);
		}
	}

}
